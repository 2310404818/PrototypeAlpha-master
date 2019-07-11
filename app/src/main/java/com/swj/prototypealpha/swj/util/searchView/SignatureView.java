package com.swj.prototypealpha.swj.util.searchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * 更加平滑的手写板
 * @author wtt
 * @date 2017/06/27
 */
public class SignatureView extends View {
    private static final float STROKE_WIDTH = 10f;

    /** Need to track this so the dirty region can accommodate the stroke. **/
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

    private Paint paint = new Paint();
    private Path path = new Path();

    /**
     * Optimizes painting by invalidating the smallest possible area.
     */
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    private Bitmap cachebBitmap;
    private Canvas cacheCanvas;


    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);

        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHehght = dm.heightPixels;
        cachebBitmap = Bitmap.createBitmap( (int) (screenWidth*0.95),(int) (screenHehght*0.4), Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.WHITE);

    }

    public Bitmap getCachebBitmap() {
        return cachebBitmap;
    }

    /**
     * Erases the signature.
     */
    public void clear() {
        if (cacheCanvas != null) {
            path.reset();

            paint.setColor(Color.WHITE);
            cacheCanvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);

            // Repaints the entire view.
            invalidate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                // There is no end point yet, so don't waste cycles invalidating.
                return true;

            case MotionEvent.ACTION_MOVE:
                if (listener!=null) {
                    listener.onMyMove();
                }
            case MotionEvent.ACTION_UP:
                // Start tracking the dirty region.
                cacheCanvas.drawPath(path, paint);
                resetDirtyRect(eventX, eventY);

                // When the hardware tracks events faster than they are delivered,
                // the
                // event will contain a history of those skipped points.
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }

                // After replaying history, connect the line to the touch point.
                path.lineTo(eventX, eventY);
                break;

            default:
//			debug("Ignored touch event: " + event.toString());
                return false;
        }

        // Include half the stroke width to avoid clipping.
        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH), (int) (dirtyRect.top - HALF_STROKE_WIDTH), (int) (dirtyRect.right + HALF_STROKE_WIDTH), (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * points.
     */
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     * 运动发生时重置脏地区
     */
    private void resetDirtyRect(float eventX, float eventY) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
        int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }

        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;

        Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (cachebBitmap != null) {
            newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
        }
        cachebBitmap = newBitmap;
        cacheCanvas = newCanvas;
    }

    private OnMyMoveListener listener;
    public void setOnMyMoveListener (OnMyMoveListener listener){
        this.listener = listener;
    }
    public interface OnMyMoveListener {
        public void onMyMove();
    }
}