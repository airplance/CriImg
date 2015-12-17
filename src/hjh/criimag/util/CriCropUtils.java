package hjh.criimag.util;

import hjh.criimag.view.CropActivity;
import hjh.criimag.view.PopPhotoView.PhotoCall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

/**
 * @author Voctex
 * 
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class CriCropUtils {

	protected Context mContext;
	private static CriCropUtils cropUtils;

	public static CriCropUtils getInstance(Context mContext) {
		if (cropUtils == null) {
			cropUtils = new CriCropUtils();
		}
		cropUtils.mContext = mContext;
		return cropUtils;
	}

	public final static String IMAGE_URI = "image_uri";
	public final static String CROP_IMAGE_URI = "crop_image_uri";
	private final int ONE_K = 1024;
	private final int ONE_M = ONE_K * ONE_K;
	private final int MAX_AVATAR_SIZE = 2 * ONE_M; // 2M

	/**
	 * ��ʼ�ü�
	 * 
	 * @Title: startPhotoCrop
	 * @param uri
	 * @param duplicatePath
	 * @param reqCode
	 * @return void
	 */
	public void startPhotoCrop(Uri uri, String duplicatePath, int reqCode) {

		preCrop(uri, duplicatePath);

		Intent intent = new Intent(mContext, CropActivity.class);
		intent.putExtra("topname", "����ͷ��");
		intent.putExtra(IMAGE_URI, uri);
		((Activity) mContext).startActivityForResult(intent, reqCode);

	}

	/**
	 * ����֮ǰ��Ԥ����
	 * 
	 * @Title: preCrop
	 * @param uri
	 * @param duplicatePath
	 * @return
	 * @return Uri
	 */
	private Uri preCrop(Uri uri, String duplicatePath) {
		Uri duplicateUri = null;

		if (duplicatePath == null) {
			duplicateUri = getDuplicateUri(uri);
		} else {
			duplicateUri = getDuplicateUri(uri, duplicatePath);
		}

		// rotateImage();

		return duplicateUri;
	}

	public Bitmap readTopImage(Intent data, PhotoCall photoCall) {
		if (data == null) {
			return null;
		} else {

			Uri uri = data.getParcelableExtra(CROP_IMAGE_URI);
			Bitmap photo = getBitmap(uri);
			CriPngSave.saveFile(photo, CriPngSave.PHOTO_PATH);
			if (photo != null) {
				if (photoCall != null) {
					photoCall.dealPortrait(photo);
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				byte[] datas = null;
				try {
					datas = baos.toByteArray();
					baos.flush();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (datasException(datas)) {
					return null;
				}
			}
			return photo;
		}
		// GetPhoto.deletefile();
	}

	/**
	 * �˴�д��������
	 * 
	 * @Title: handleDatasException
	 * @param datas
	 * @return void
	 */
	private boolean datasException(byte[] datas) {
		// ͷ�����쳣
		if (datas == null || datas.length <= 0) {

			return true;
		}

		// ͷ��ߴ粻��
		if (datas.length > MAX_AVATAR_SIZE) {

			return true;
		}

		return false;
	}

	/**
	 * �˴�д��������
	 * 
	 * @Title: getBitmap
	 * @param intent
	 * @return void
	 */
	public Bitmap getBitmap(Uri uri) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {

			is = getInputStream(uri);

			bitmap = BitmapFactory.decodeStream(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
		return bitmap;
	}

	/**
	 * ��ȡ������
	 * 
	 * @Title: getInputStream
	 * @param mUri
	 * @return
	 * @return InputStream
	 */
	private InputStream getInputStream(Uri mUri) throws IOException {
		try {
			if (mUri.getScheme().equals("file")) {
				return new java.io.FileInputStream(mUri.getPath());
			} else {
				return ((Activity) mContext).getContentResolver()
						.openInputStream(mUri);
			}
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	/**
	 * ���û�ȡ�ü���ͼ���uri
	 * 
	 * @Title: getDuplicateUri
	 * @param uri
	 * @return
	 * @return Uri
	 */
	private Uri getDuplicateUri(Uri uri) {
		Uri duplicateUri = null;

		String uriString = getUriString(uri);

		duplicateUri = getDuplicateUri(uri, uriString);

		return duplicateUri;
	}

	/**
	 * ��������յĻ���ֱ�ӻ�ȡ��
	 * 
	 * @Title: getDuplicateUri
	 * @param uri
	 * @param uriString
	 * @return
	 * @return Uri
	 */
	private Uri getDuplicateUri(Uri uri, String uriString) {

		Uri duplicateUri = null;
		String duplicatePath = null;
		duplicatePath = uriString.replace(".", "_duplicate.");
		// �ж�ԭͼ�Ƿ���ת����ת�˽����޸�
		rotateImage(uriString);

		duplicateUri = Uri.fromFile(new File(duplicatePath));

		return duplicateUri;
	}

	/**
	 * ����Uri��ȡ�ļ���·��
	 * 
	 * @Title: getUriString
	 * @param uri
	 * @return
	 * @return String
	 */
	private String getUriString(Uri uri) {
		String imgPath = null;
		if (uri != null) {
			String uriString = uri.toString();
			// С���ֻ����������⣬С���ֻ���uri��file��ͷ���������ֻ�����content��ͷ
			// ��content��ͷ��uri����ͼƬ�������ݿ����ˣ�����file��ͷ��ʾû�в������ݿ�
			// ���ԾͲ���ͨ��query����ѯ�������ȡ��cursor��Ϊnull��
			if (uriString.startsWith("file")) {
				// uri�ĸ�ʽΪfile:///mnt....,��ǰ�߸����˵���ȡ·��
				imgPath = uriString.substring(7, uriString.length());

				return imgPath;
			}
			if (uriString.startsWith("content://")) {// content://media/external/images
				Cursor cursor = mContext.getContentResolver().query(uri, null,
						null, null, null);
				cursor.moveToFirst();
				imgPath = cursor.getString(1); // ͼƬ�ļ�·��
				Log.e("imgPath", "" + imgPath);
			} else {
				imgPath = getAbsoluteImagePath(uri);
				Log.e("imgPath", "" + imgPath);
			}
		}
		return imgPath;
	}

	/**
	 * ��תͼ��
	 * 
	 * @Title: rotateImage
	 * @return void
	 */
	private void rotateImage(String uriString) {

		try {
			ExifInterface exifInterface = new ExifInterface(uriString);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90
					|| orientation == ExifInterface.ORIENTATION_ROTATE_180
					|| orientation == ExifInterface.ORIENTATION_ROTATE_270) {

				String value = String.valueOf(orientation);
				exifInterface
						.setAttribute(ExifInterface.TAG_ORIENTATION, value);
				exifInterface.saveAttributes();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �˷����Ǹ���uri������ȷ��·��
	 * 
	 * @author ��
	 * */
	@SuppressWarnings("deprecation")
	private String getAbsoluteImagePath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = ((Activity) mContext).managedQuery(uri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
