package com.proinlab.functions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * SDCARD/.Impression/ Category Name / File Type / FileDir / 1~n.png
 * 
 * @author PROIN LAB
 */

public class FileManager {

	/**
	 * ������ �����Ѵ�
	 * 
	 * @param FileID
	 * @return
	 */
	public boolean CREATE_FILE_FODLER(String FileID) {
		File FILE = new File(B.SDCARD_DIRECTORY + "FILE/" + FileID);
		if (!FILE.exists())
			while (FILE.mkdirs())
				;
		return true;
	}

	/**
	 * ���� ������ �����Ѵ�
	 * 
	 * @param FileDir
	 * @return
	 */
	public boolean DELETE_FILE_FODLER(String FileDir) {

		String mPath = B.SDCARD_DIRECTORY + "FILE/" + FileDir;
		DeleteDir(mPath);

		return true;
	}

	public static void DeleteDir(String path) {
		File file = new File(path);
		if (!file.exists())
			return;

		File[] childFileList = file.listFiles();
		for (File childFile : childFileList) {
			if (childFile.isDirectory()) {
				DeleteDir(childFile.getAbsolutePath());
			} else {
				childFile.delete();
			}
		}
		file.delete();
	}

	/**
	 * ���� ȭ���� �����Ѵ�
	 * 
	 * @param foreground
	 * @param saveDir
	 *            : ���ϸ���� ���� ���
	 * @return
	 */
	public boolean SAVE_SINGLE_PAGE(Bitmap foreground, String saveDir) {
		File file = new File(saveDir);
		if (!file.exists())
			while (file.mkdir())
				;
		String savePath = saveDir;
		if (foreground != null)
			saveBitmapPNG(savePath, foreground);
		return true;
	}

	/**
	 * ��ο� �ִ� �̹����� ���İ��� ������� �����Ѵ�
	 * 
	 * @param imgDir
	 *            �̹����� ��ü ���
	 * @param parentPath
	 *            �̹����� �θ� ���
	 * @param alpha
	 * @return
	 */
	public boolean PNG_ALPHACHANGE_BY_DIR(String imgDir, String parentPath,
			int alpha) {
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imgDir, opts);
		
		if (bitmap == null)
			return false;

		bitmap = SetBitmapAlpha(bitmap, alpha, bitmap.getWidth(),
				bitmap.getHeight());
		saveBitmapPNG(parentPath + "background.png", bitmap);

		return true;
	}

	/**
	 * ��ü ���������� ����
	 * 
	 * @param Dir
	 *            ��ü ���
	 * @return
	 */
	public int GET_LENGTH_SAVE_PAGES(String Dir) {
		File file = new File(Dir);
		String[] lists = file.list();
		int returnnum = 0;
		if (lists != null)
			returnnum = lists.length;
		return returnnum;
	}

	/**
	 * �ش� �������� �����Ѵ�
	 * 
	 * @param Dir
	 *            ex) ~~/~~/dir
	 * @param page
	 */
	public void DELETE_PAGE(String Dir, int page) {
		File file = new File(Dir);
		String[] lists = file.list();

		File delete = new File(Dir + "/" + Integer.toString(page) + ".png");
		delete.delete();

		for (int i = page + 1; i < lists.length + 1; i++) {
			File rename = new File(Dir + "/" + Integer.toString(i) + ".png");
			File target = new File(Dir + "/" + Integer.toString(i - 1) + ".png");
			rename.renameTo(target);
		}
	}
	
	private boolean saveBitmapPNG(String FileDir, Bitmap bitmap) {
		if (FileDir == null || bitmap == null)
			return false;

		File saveFile = new File(FileDir);

		if (saveFile.exists()) {
			while (!saveFile.delete())
				;
		}

		try {
			saveFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(saveFile);
			bitmap.compress(CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
	
	private Bitmap SetBitmapAlpha(Bitmap org_bitmap, int alpha, int width,
			int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Paint p = new Paint();
		p.setDither(true);
		p.setFlags(Paint.ANTI_ALIAS_FLAG);
		p.setAlpha(alpha);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(org_bitmap, 0, 0, p);

		return bitmap;
	}
}
