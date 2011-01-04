package com.hlidskialf.android.gameshell;



import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;


public class GamepadOverlayView extends View
{

    public GamepadOverlayView(Context context) { this(context,null,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs) { this(context,attrs,0); }
    public GamepadOverlayView(Context context, AttributeSet attrs, int style)
    {
        super(context,attrs,style);
    }

    protected void onDraw(Canvas canvas)
    {
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
    }

}
