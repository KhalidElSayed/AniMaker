package com.proinlab.canvasview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;

	private int penstyle = AboutPen.Pen;

	private int width, height;

	/**
	 * CanvasView, ������� �� �� �ִ� �� ��ü�� �������ش�.
	 * 
	 * @param context
	 */

	public CanvasView(Context context) {
		super(context);

		mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		setPenStyle(AboutPen.Pen);

	}

	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		if (penstyle == AboutPen.ERASER)
			;
		else
			canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	/*--------- �⺻ ���� ----------*/

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
		if (penstyle == AboutPen.ERASER) {
			mCanvas.drawPath(mPath, mPaint);
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		mPath = new Path();
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			break;
		}
		invalidate();
		return true;
	}

	/**
	 * Canvas�� ��Ʈ������ �����´�
	 * 
	 * @return
	 */
	public Bitmap getWorkspaceBitmap() {
		return mBitmap;
	}

	/**
	 * �۾����� ��Ʈ���� �ҷ��´�
	 * 
	 * @param bitmap
	 */
	public void setWorkspaceBitmap(Bitmap bitmap) {
		if(bitmap==null)
			return;
		mBitmap = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
		bitmap.recycle();
		invalidate();
	}

	/**
	 * ���� �۾�
	 * 
	 * @return
	 */
	public boolean undo() {
		return true;
	}

	/**
	 * �ǵ�����
	 * 
	 * @param bitmap
	 */
	public boolean redo() {
		return true;
	}

	/**
	 * Canvas�� ����� �������ش�
	 * 
	 * @param width
	 * @param height
	 */
	public void initializeCanvas(int width, int height) {
		this.width = width;
		this.height = height;
		mBitmap = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		invalidate();
	}

	/**
	 * ���� ������ �������ش�. Default : Black
	 * 
	 * @param color
	 */
	public void setPenColor(int color) {
		mPaint.setColor(color);
	}

	/**
	 * ���� �β��� �������ش�. Default : 4
	 * 
	 * @param penWidth
	 */	
	public void setPenSize(int size) {
		mPaint.setStrokeWidth(size);
	}

	/**
	 * ���� ��Ÿ���� �������ش�. AboutPen.penStyle
	 * 
	 * @param penStyle
	 */
	public void setPenStyle(int penStyle) {
		penstyle = penStyle;
		switch (penStyle) {
		case AboutPen.Pen:
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(4);
			break;
		case AboutPen.MARKER:
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setColor(Color.rgb(206, 230, 185));
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(50);
			mPaint.setAlpha(100);
			break;
		case AboutPen.ERASER:
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAlpha(0);
			mPaint.setStrokeWidth(50);
			break;
		}
	}

	/**
	 * ���� ������ �������ش�
	 * 
	 * @param alpha
	 */
	public void setPenAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	/**
	 * ���� ������ ��ȯ�Ѵ�
	 * 
	 * @return int color
	 */
	public int getPenColor() {
		return mPaint.getColor();
	}

	/**
	 * ���� ������ ��ȯ�Ѵ�
	 * 
	 * @return int Alpha
	 */
	public int getPenAlpha() {
		return mPaint.getAlpha();
	}

	/**
	 * ���� �β��� ��ȯ�Ѵ�
	 * 
	 * @return
	 */
	public int getPenWidth() {
		return (int) mPaint.getStrokeWidth();
	}
	
	/**
	 * CanvasView�� �޸𸮻󿡼� free��Ų��.
	 */
	public void distroyView() {
		mBitmap.recycle();
	}
	
}
