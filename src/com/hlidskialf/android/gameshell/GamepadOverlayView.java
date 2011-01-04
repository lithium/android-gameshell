package com.hlidskialf.android.gameshell;



import android.view.View;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Paint;
import android.util.Log;
import android.util.FloatMath;


public class GamepadOverlayView extends View
{
    private static final float JOYSTICK_RADIUS=60f;


    /* public */

    public void setOnJoystickListener(OnJoystickListener lstnr)
    {
        mJoystickListener = lstnr;
    }

    public interface OnJoystickListener {
        public void onJoystickEvent(float x, float y);
    }

    public interface OnDpadListener {
        public void onDpadEvent(int direction);
    }



    public GamepadOverlayView(Context context) { this(context,null,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs) { this(context,attrs,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs, int style)
    {
        super(context,attrs,style);

        paint_radius = new Paint();
        paint_radius.setColor(Color.BLUE);
        paint_radius.setStyle(Paint.Style.FILL);
        paint_radius.setAlpha(32);

        paint_stick = new Paint();
        paint_stick.setColor(Color.RED);
        paint_stick.setStyle(Paint.Style.STROKE);
        paint_stick.setStrokeWidth(.5f);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.save();


        if (mIsJoystick) {
            canvas.save();
            canvas.drawCircle(mJoystickOrigin.x, mJoystickOrigin.y, JOYSTICK_RADIUS, paint_radius);

            if (mJoystickLast.x != -1) {
                canvas.drawLine(mJoystickOrigin.x, mJoystickOrigin.y, 
                    mJoystickLast.x, mJoystickLast.y, paint_stick);
            }
            canvas.restore();
        }


        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getActionMasked();
        switch (action)
        {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                mIsJoystick = true;
                mJoystickOrigin.set(ev.getX(), ev.getY());
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mJoystickListener != null) {
                    float x = ev.getX();
                    float y = ev.getY();
                    mJoystickLast.set(x,y);

                    x = (x - mJoystickOrigin.x) / JOYSTICK_RADIUS;
                    y = (y - mJoystickOrigin.y) / JOYSTICK_RADIUS;

                    x = x < -1f ? -1f : x > 1f ? 1f : x;
                    y = y < -1f ? -1f : y > 1f ? 1f : y;

                    if (mJoystickListener != null)
                        mJoystickListener.onJoystickEvent(x,y);

                    invalidate();
                }
                break;
        }
        return true;
    }

    /* private */
    OnJoystickListener mJoystickListener;

    int mWidth, mHeight;
    PointF mJoystickOrigin = new PointF(-1f,-1f);
    boolean mIsJoystick = false;
    PointF mJoystickLast = new PointF(-1f,-1f);

    PointF mButtonsOrigin = new PointF(-1f,-1f);


    Paint paint_radius;
    Paint paint_stick;
}
