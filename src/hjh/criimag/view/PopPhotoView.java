package hjh.criimag.view;

import hjh.criimag.R;
import hjh.criimag.util.CirPictureUtils;
import hjh.criimag.util.CriCropUtils;
import hjh.criimag.util.CriPngSave;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cxh.voctex.utils.ToastUtil;

/**
 * @author cxh 2015-05-25
 * 
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class PopPhotoView extends PopupWindow {

	private Context mContext;
	private int[] textIds = new int[] { R.id.take_photo_text,
			R.id.cancel_photo_text, R.id.select_photo_text };

	public int isPhotoPath = 0;

	// private String[] popStrings = { "���ѡ��", "�������" };
	/* ������ */
	public static final int IMAGE_REQUEST_CODE = 0;
	public static final int CAMERA_REQUEST_CODE = 1;
	public static final int RESULT_REQUEST_CODE = 2;
	private String TEMP_IMAGE_PATH;

	private ImageView CirImg;
	private View popView;// Pop��View����
	private int showViewId; // Pop����show��ʱ����Ҫһ��id������

	public PopPhotoView(Context mContext, View view) {
		super(view, -1, -1);
		this.popView = view;
		this.mContext = mContext;
	}

	/**
	 * 
	 * @param mContext
	 *            ��Ϊnull�������
	 * @param layoutId
	 *            ��Ϊ0�������
	 * @param showViewId
	 *            ����ҪĬ�ϵ�CirImg����¼�����Ϊ0���粻��ҪĬ�ϵ�CirImg����¼������
	 * @param CirImg
	 *            ����ҪĬ�ϵ�photoCall��������Ĭ�ϵ�CirImg����¼������벻Ϊnull
	 * @param photoCall
	 *            �ǲ�����ҪĬ�ϵ�photoCall�������ǵĻ���null
	 * @param OnClick
	 *            �ǲ�����ҪĬ�ϵ�CirImg����¼����ǵĻ���null
	 */
	public PopPhotoView(Context mContext, int layoutId, int showViewId,
			int[] textId, ImageView CirImg, PhotoCall photoCall,
			OnClickListener OnClick) {
		this(mContext, LayoutInflater.from(mContext).inflate(layoutId, null));
		this.showViewId = showViewId;
		if (photoCall == null) {
			photoCall = defaultPhotoCall;
		}
		this.photoCall = photoCall;
		if (OnClick == null) {
			OnClick = defaultOnClick;
		}
		this.OnClick = OnClick;
		if (this.CirImg == null) {
			this.CirImg = CirImg;
			this.CirImg.setOnClickListener(this.OnClick);
		}
		this.textIds = textId;
	}

	@SuppressWarnings("unused")
	private PopPhotoView(Context mContext) {
		super();
	}

	private class IOnClick extends hjh.criimag.util.CriIntervalOnClick {

		public IOnClick(int interval_time) {
			super(interval_time);
		}

		@Override
		public void IonClick(View v) {
			// TODO Auto-generated method stub

			if (v.getId() == textIds[0]) {
				Intent cCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				cCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				((Activity) mContext).startActivityForResult(cCamera,
						CAMERA_REQUEST_CODE);
				dismiss();
			} else if (v.getId() == textIds[1]) {
				Intent cLocal = new Intent();
				cLocal.setType("image/*");// �ļ�����
				cLocal.setAction(Intent.ACTION_GET_CONTENT);
				((Activity) mContext).startActivityForResult(cLocal,
						IMAGE_REQUEST_CODE);
				dismiss();
			} else if (v.getId() == textIds[2]) {
				dismiss();
			} else {
				// ����ǵ��CirImg��
				if (showViewId != -1) {
					showView(((Activity) mContext).findViewById(showViewId));
				} else {
					ToastUtil.showS(mContext, "showViewId=-1");
				}
			}

		}
	}

	private PhotoCall defaultPhotoCall = new PhotoCall() {

		@Override
		public void dealPortrait(Bitmap bitmap) {
			// TODO Auto-generated method stub
			if (CirImg != null) {
				CirImg.setImageBitmap(bitmap);
			} else {
				ToastUtil.showS(mContext, "CirImg=null");
			}
		}
	};

	private IOnClick defaultOnClick = new IOnClick(500);

	public void initView() {

		for (int i = 0; i < textIds.length; i++) {
			TextView textView = (TextView) popView.findViewById(textIds[i]);
			textView.setOnClickListener(new IOnClick(500));
		}

		setFocusable(true);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		setBackgroundDrawable(dw);
		// setBackgroundDrawable(new BitmapDrawable());
		// setAnimationStyle(R.style.pop_anim_style);// pop����

		// ��ȡ�ⲿ�洢Ŀ¼
		TEMP_IMAGE_PATH = CriPngSave.PIC_PATH + "picture.png";
		photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
	}

	private Uri photoUri;

	public void showView(View v) {
		if (v != null) {
			showAtLocation(v, Gravity.BOTTOM, 0, 0);
		}
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	@SuppressWarnings("unused")
	private void startPhotoZoom(Uri uri) throws ActivityNotFoundException {
		// try {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		((Activity) mContext).startActivityForResult(intent, 2);
		// } catch (ActivityNotFoundException e) {
		// // TODO: handle exception
		// ToastUtil.showS(mContext, "δ���ֽ�ͼ����");
		// }
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	@SuppressWarnings("unused")
	private void getImageToView(Intent data) {

		if (data == null) {
			return;
		} else {

			Uri uri = data.getParcelableExtra(CriCropUtils.CROP_IMAGE_URI);
			Bitmap photo = CriCropUtils.getInstance(mContext).getBitmap(uri);
			// Bundle extras = data.getExtras();
			// if (extras != null) {
			// photo = extras.getParcelable(CropUtils.CROP_IMAGE_URI);
			if (photo != null) {
				photo = CirPictureUtils.zoomBitmap(photo, 145, 145);
				CirPictureUtils.savePhotoToSDCard(photo, CriPngSave.PIC_PATH,
						"photo");
				if (photoCall != null) {
					photoCall.dealPortrait(photo);
				}
			}

			// }
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ����벻����ȡ��ʱ��
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:// ���
				isPhotoPath = IMAGE_REQUEST_CODE;
				if (data != null) {
					try {
						// startPhotoZoom(data.getData());
						CriCropUtils.getInstance(mContext).startPhotoCrop(
								data.getData(), null, RESULT_REQUEST_CODE);
					} catch (ActivityNotFoundException e) {
						// TODO: handle exception
						ToastUtil.showS(mContext, "ͼƬ������", true);
					}
				}
				break;
			case CAMERA_REQUEST_CODE:// ����
				isPhotoPath = CAMERA_REQUEST_CODE;
				try {
					if (data != null) {
						// startPhotoZoom(data.getData());
						CriCropUtils.getInstance(mContext).startPhotoCrop(
								data.getData(), null, RESULT_REQUEST_CODE);
					} else {
						// startPhotoZoom(photoUri);
						CriCropUtils.getInstance(mContext).startPhotoCrop(
								photoUri, null, RESULT_REQUEST_CODE);
					}
				} catch (ActivityNotFoundException e) {
					// TODO: handle exception
					ToastUtil.showS(mContext, "ͼƬ������", true);
				}
				break;
			case RESULT_REQUEST_CODE:// ��ͼ
				if (data != null) {
					CriCropUtils.getInstance(mContext).readTopImage(data,
							photoCall);
					// getImageToView(data);
				}
				break;
			}
		}
	}

	public interface PhotoCall {
		void dealPortrait(Bitmap bitmap);
	}

	private PhotoCall photoCall;

	public void setPhotoCall(PhotoCall photoCall) {
		this.photoCall = photoCall;
	}

	private OnClickListener OnClick;

}
