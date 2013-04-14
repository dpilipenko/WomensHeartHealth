package com.fima.chartview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

public class LinearSeries extends AbstractSeries {
	private PointF mLastPoint;

	@Override
	public void drawPoint(Canvas canvas, AbstractPoint point, float scaleX, float scaleY, Rect gridBounds) {
		final float x = (float) (gridBounds.left + (scaleX * (point.getX() - getMinX())));
		final float y = (float) (gridBounds.bottom - (scaleY * (point.getY() - getMinY())));

		if (mLastPoint != null) {
			canvas.drawLine(mLastPoint.x, mLastPoint.y, x, y, mPaint);
		}
		else {
			mLastPoint = new PointF();
		}

		mLastPoint.set(x, y);

		canvas.drawCircle(x, y, 6, mPaint);
		
	}

	@Override
	protected void onDrawingComplete() {
		mLastPoint = null;
	}

	public static class LinearPoint extends AbstractPoint {
		public LinearPoint() {
			super();
		}

		public LinearPoint(double x, double y) {
			super(x, y);
		}

		@Override
		public String toString() {
			return "("+getX()+","+getY()+")";
		}
		
		
	}
}