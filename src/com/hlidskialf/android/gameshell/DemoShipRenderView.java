package com.hlidskialf.android.gameshell;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.Math;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class DemoShipRenderView extends GLSurfaceView

{

    public DemoShipRenderView(Context context) { this(context,null); }
    public DemoShipRenderView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }

    public void start()
    {
        mHandler = new Handler();
        mRenderer = new ShipRenderer();
        setRenderer(mRenderer);
    }




    /* private */
    Handler mHandler;
    GLSurfaceView.Renderer mRenderer;


    private class ShipRenderer implements GLSurfaceView.Renderer
    {

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
        }

        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
        }

        public void onDrawFrame(GL10 gl)
        {
        }

    }

}
