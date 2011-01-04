package com.hlidskialf.android.gameshell;



import android.view.View;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Point;


public class GamepadOverlayView extends View
{

    public GamepadOverlayView(Context context) { this(context,null,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs) { this(context,attrs,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs, int style)
    {
        super(context,attrs,style);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
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
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return super.onTouchEvent(ev);
    }



    /* private */

    int mWidth, mHeight;
    Point mJoystickOrigin = new Point();
    Point mButtonsOrigin = new Point();
}
