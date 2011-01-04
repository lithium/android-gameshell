package com.hlidskialf.android.gameshell;



import android.view.View;
import android.view.MotionEvent;
import android.view.GestureDetector;
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
    private static final float BUTTON_RADIUS=30f;
    private static final float BUTTON_OFFSET=38f;




    /* public */
    public static final int BUTTON_A=1<<0;
    public static final int BUTTON_B=1<<1;


    public void setOnJoystickListener(OnJoystickListener lstnr)
    {
        mJoystickListener = lstnr;
    }
    public void setOnButtonsListener(OnButtonsListener lstnr)
    {
        mButtonsListener = lstnr;
    }

    public interface OnJoystickListener {
        public void onJoystickEvent(float x, float y);
        public void onJoystickUp();
    }

    public interface OnDpadListener {
        public void onDpadEvent(int direction);
    }

    public interface OnButtonsListener {
        public void onButtonDown(int which);
        public void onButtonUp(int which);
    }




    public GamepadOverlayView(Context context) { this(context,null,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs) { this(context,attrs,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs, int style)
    {
        super(context,attrs,style);

        mGestureDetector = new GestureDetector(context, new ButtonGestureDetector());

        paint_radius = new Paint();
        paint_radius.setColor(Color.BLUE);
        paint_radius.setStyle(Paint.Style.FILL);
        paint_radius.setAlpha(64);

        paint_stick = new Paint();
        paint_stick.setColor(Color.RED);
        paint_stick.setStyle(Paint.Style.STROKE);
        paint_stick.setStrokeWidth(.5f);

        paint_button = new Paint();
        paint_button.setColor(Color.GREEN);
        paint_button.setStyle(Paint.Style.STROKE);
        paint_button.setStrokeWidth(1f);

        paint_button_press = new Paint();
        paint_button_press.setColor(Color.RED);
        paint_button_press.setStyle(Paint.Style.FILL);
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

        if (mIsButton) {
            canvas.save();

            canvas.drawCircle(mButtonsOrigin.x - BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, 
                (mButtonState & BUTTON_B) == BUTTON_B ? paint_button_press : paint_button);

            canvas.drawCircle(mButtonsOrigin.x + BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, 
                (mButtonState & BUTTON_A) == BUTTON_A ? paint_button_press : paint_button);

            canvas.restore();
        }


        if (mTouchRadius > 0) {
            canvas.save();

            canvas.drawCircle(mTouchOrigin.x, mTouchOrigin.y, mTouchRadius, paint_radius);

            canvas.restore();
        }


        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int index = ev.getActionIndex();
        int pointer = ev.getPointerId(index);
        float x = ev.getX(index);

        if (x < mWidth/2) {
            joystick_touch(ev);
        }
        else if (!mIsJoystick || mJoystickPointerId != pointer) {
            if (!mGestureDetector.onTouchEvent(ev)) {
                if (mIsButton) {
                    button_touch(ev);
                }
            }
        }

        return true;
    }






    /* private */
    float mWidth, mHeight;

    PointF mJoystickOrigin = new PointF(-1f,-1f);
    boolean mIsJoystick = false;
    PointF mJoystickLast = new PointF(-1f,-1f);
    int mJoystickPointerId = -1;

    PointF mButtonsOrigin = new PointF(-1f,-1f);
    boolean mIsButton = false;
    int mButtonAPointerId = -1;
    int mButtonBPointerId = -1;
    int mButtonState;


    PointF mTouchOrigin = new PointF(-1f,-1f);
    float mTouchRadius = -1;


    Paint paint_radius;
    Paint paint_stick;
    Paint paint_button;
    Paint paint_button_press;

    OnJoystickListener mJoystickListener;
    OnButtonsListener mButtonsListener;
    GestureDetector mGestureDetector;




    private void joystick_touch(MotionEvent ev)
    {
        int action = ev.getActionMasked();
        int index = ev.getActionIndex();
        int pointer = ev.getPointerId(index);
        switch (action)
        {
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (mIsJoystick && pointer == mJoystickPointerId) {
                    mIsJoystick = false;
                    mJoystickPointerId = -1;
                    if (mJoystickListener != null) {
                        mJoystickListener.onJoystickUp();
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                if (!mIsJoystick) {
                    mIsJoystick = true;
                    mJoystickOrigin.set(ev.getX(index), ev.getY(index));
                    mJoystickPointerId = pointer;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsJoystick && pointer == mJoystickPointerId) {
                    float x = ev.getX(index);
                    float y = ev.getY(index);
                    mJoystickLast.set(x,y);

                    x = (x - mJoystickOrigin.x) / JOYSTICK_RADIUS;
                    y = (y - mJoystickOrigin.y) / JOYSTICK_RADIUS;

                    x = x < -1f ? -1f : x > 1f ? 1f : x;
                    y = y < -1f ? -1f : y > 1f ? 1f : y;

                    if (mJoystickListener != null) {
                        mJoystickListener.onJoystickEvent(x,y);
                    }
                    invalidate();
                }
                break;
        }
    }

    private void button_touch(MotionEvent ev)
    {
        int action = ev.getActionMasked();
        int index = ev.getActionIndex();
        int pointer = ev.getPointerId(index);
        switch (action)
        {
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mButtonState = 0;
                int which=0;
                if (pointer == mButtonAPointerId) {
                    which = BUTTON_A;
                    mButtonAPointerId = -1;
                }
                else
                if (pointer == mButtonBPointerId) {
                    which = BUTTON_B;
                    mButtonBPointerId = -1;
                }

                if (mButtonsListener != null)  {
                    mButtonsListener.onButtonUp(which);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                float x = ev.getX(index);
                float y = ev.getY(index);
                float r = (ev.getSize(index) * mWidth)/2f;

                Log.v("Gamepad", "button == joystick :: "+y+" = "+mJoystickLast.y);

                mTouchOrigin.set(x,y);
                mTouchRadius = r;

                if (circle_collide(mButtonsOrigin.x - BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, x,y,r)) {
                    // B button pressed
                    mButtonState |= BUTTON_B;
                    mButtonBPointerId = pointer;


                    if (mButtonsListener != null) 
                        mButtonsListener.onButtonDown(BUTTON_B);

                } else {
                    mButtonState &= ~BUTTON_B;
                }

                if (circle_collide(mButtonsOrigin.x + BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, x,y,r)) {
                    // A button pressed
                    mButtonState |= BUTTON_A;
                    mButtonAPointerId = pointer;

                    if (mButtonsListener != null) 
                        mButtonsListener.onButtonDown(BUTTON_A);
                } else {
                    mButtonState &= ~BUTTON_A;
                }

                invalidate();
                break;

        }
    }

    private class ButtonGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        public void onLongPress(MotionEvent ev)
        {
            int index = ev.getActionIndex();
            float x = ev.getX(index);
            float y = ev.getY(index);
            float r = (ev.getSize(index) * mWidth)/2f;

            if (circle_collide(mButtonsOrigin.x - BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, x,y,r) ||
                circle_collide(mButtonsOrigin.x + BUTTON_OFFSET, mButtonsOrigin.y, BUTTON_RADIUS, x,y,r)) {
                // long pressed on a button; ignore
                return;
            }

            mIsButton = true;
            mButtonsOrigin.set(ev.getX(index), ev.getY(index));

            invalidate();
        }
    }



    private static final boolean circle_collide(float x1, float y1, float r1, float x2, float y2, float r2)
    {
        float x = x2 - x1;
        float y = y2 - y1;
        float distance = FloatMath.sqrt(x*x + y*y);
        return (distance < r1+r2);
    }
}
