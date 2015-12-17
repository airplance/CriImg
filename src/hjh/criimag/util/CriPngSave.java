package hjh.criimag.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class CriPngSave {
	public static final String PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/YiDont_Phone";
	public static String PIC_PATH = PATH + "/picture/";
	public static String PHOTO_PATH = PIC_PATH + "photo.png";
	
	/**
	 * 保存图片到app指定路径
	 * 
	 * @param bm头像图片资源
	 * @param fileName保存名称
	 */
	public static boolean saveFile(Bitmap bm, String filePath) {
		if (bm == null)
			return false;
		if (getSDCardStatus()) {
			try {
				String Path = filePath.substring(0, filePath.lastIndexOf("/"));
				File dirFile = new File(Path);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				File myCaptureFile = new File(filePath);
				BufferedOutputStream bo = null;
				bo = new BufferedOutputStream(new FileOutputStream(
						myCaptureFile));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, bo);
				bo.flush();
				bo.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断SD卡是否可用
	 */
	public static boolean getSDCardStatus() {
		boolean SDStatus = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (SDStatus) {
			return true;
		} else {
			return false;
		}
	}
}
