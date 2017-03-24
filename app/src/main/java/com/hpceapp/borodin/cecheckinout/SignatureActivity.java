package com.hpceapp.borodin.cecheckinout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;

public class SignatureActivity extends Activity
{
	private static final String TAG = "SignatureActivity_TEST";
	private signature mSignature;
	private Paint paint;
	private LinearLayout mContent;
	private Button clear, save;
	private String managerName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);

		save = (Button) findViewById(R.id.sig_save);
		save.setEnabled(false);
		clear = (Button) findViewById(R.id.sig_clear);
		mContent = (LinearLayout) findViewById(R.id.mysignatyre);

		mSignature = new signature(this);
		mContent.addView(mSignature);

		save.setOnClickListener(onButtonClick);
		clear.setOnClickListener(onButtonClick);

		managerName = getIntent().getExtras().getString(getString(R.string.TheManagerName));
	}

	Button.OnClickListener onButtonClick = new Button.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v == clear)
			{
				mSignature.clear();
			} else if (v == save)
			{
				mSignature.save();
			}
		}
	};

	public class signature extends View
	{
		static final float STROKE_WIDTH = 10f;
		static final float WRITE_WIDTH = 2f;
		static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		Paint paint = new Paint();
		Path path = new Path();

		float lastTouchX;
		float lastTouchY;
		final RectF dirtyRect = new RectF();

		public signature(Context context)
		{
			super(context);
			setfordraw();
		}

		private void setfordraw()
		{
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		private void setforwrit()
		{
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(WRITE_WIDTH);
		}

		public void clear()
		{
			path.reset();
			invalidate();
			save.setEnabled(false);
		}

		public void save()
		{
			Bitmap returnedBitmap = Bitmap.createBitmap(mContent.getWidth(),
					mContent.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(returnedBitmap);
			Drawable bgDrawable = mContent.getBackground();
			if (bgDrawable != null)
				bgDrawable.draw(canvas);
			else
				canvas.drawColor(Color.WHITE);
			mContent.draw(canvas);

			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			returnedBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
			Intent intent = new Intent();
			intent.putExtra("byteArray", bs.toByteArray());
			setResult(RESULT_OK, intent);
			finish();
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			// set it up for scaling to diferent screens
			int width = size.x;
			int height = size.y;
			int pw = width * 25 / 100;
			int ph = height * 15 / 100;
			int tsize = width * 10 / 100;

			setforwrit();
			canvas.save();

			// disply manager name
			canvas.rotate((float)90, pw-(tsize+3), ph);
			paint.setTextSize(tsize);
			canvas.drawText(managerName, pw, ph, paint);
			canvas.restore();
			setfordraw();
			// draw the cross
			int x  = (int)(pw*1.2);
			int y  = (int)(ph);
			int x2 = (int)(pw*1.5);
			int y2 = (int)(ph*1.3);
			Utilities.print(TAG, "Line x = " + x + " Line Y = " + y);
			canvas.drawLine( x, y, x2, y2, paint);
			canvas.drawLine(x2, y, x, y2, paint);
			// draw the line for signature
			canvas.drawLine(pw, ph, pw, height - (int)(ph*1.5), paint);
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			float eventX = event.getX();
			float eventY = event.getY();
			save.setEnabled(true);

			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					lastTouchX = eventX;
					lastTouchY = eventY;
					return true;

				case MotionEvent.ACTION_MOVE:

				case MotionEvent.ACTION_UP:

					resetDirtyRect(eventX, eventY);
					int historySize = event.getHistorySize();
					for (int i = 0; i < historySize; i++)
					{
						float historicalX = event.getHistoricalX(i);
						float historicalY = event.getHistoricalY(i);
						path.lineTo(historicalX, historicalY);
					}
					path.lineTo(eventX, eventY);
					break;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void resetDirtyRect(float eventX, float eventY)
		{
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}
}
