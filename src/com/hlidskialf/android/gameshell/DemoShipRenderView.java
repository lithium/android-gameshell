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
                implements GamepadOverlayView.OnJoystickListener

{

    public DemoShipRenderView(Context context) { this(context,null); }
    public DemoShipRenderView(Context context, AttributeSet attrs)
    {
        super(context,attrs);

        mShip = new Ship(32f);
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
    Ship mShip;
    int mViewWidth, mViewHeight;


    private class ShipRenderer implements GLSurfaceView.Renderer
    {


        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            gl.glClearColor(0f,0f,0f, 0.5f);
            gl.glDisable(GL10.GL_DEPTH_TEST);

            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        }

        public void onSurfaceChanged(GL10 gl, int w, int h)
        {
            mViewWidth = w;
            mViewHeight = h;
            gl.glViewport(0,0,w,h);

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluOrtho2D(gl, 0f, (float)w, (float)h, 0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        public void onDrawFrame(GL10 gl)
        {
            tick();

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();


            gl.glPushMatrix();


            gl.glTranslatef(mShip.origin.x, mShip.origin.y, 0f);
            mShip.draw(gl);


            gl.glPopMatrix();
        }


        private void tick()
        {
            mShip.origin.x += mShip.accel.x;
            mShip.origin.y += mShip.accel.y;

            if (mShip.origin.x > mViewWidth) mShip.origin.x = 0;
            else if (mShip.origin.x < 0) mShip.origin.x = mViewWidth;

            if (mShip.origin.y > mViewHeight) mShip.origin.y = 0;
            else if (mShip.origin.y < 0) mShip.origin.y = mViewHeight;

        }

    }



    public void onJoystickEvent(float x, float y)
    {
        float max_speed = 5f;

        //if (x > 0 || x < 0) mShip.origin.x += x*max_speed;
        //if (y > 0 || y < 0) mShip.origin.y += y*max_speed;
        mShip.accel.set(x*max_speed, y*max_speed);


        double angle = Math.atan(x / y) * 180 / Math.PI;
        angle = y > 0 ? 90 + angle : 270 + angle;

        mShip.angle = 270f - (float)angle;
    }
    public void onJoystickUp()
    {
        mShip.accel.set(0f,0f);
    }
}
