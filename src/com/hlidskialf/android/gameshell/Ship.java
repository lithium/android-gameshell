package com.hlidskialf.android.gameshell;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.PointF;

import javax.microedition.khronos.opengles.GL10;

public class Ship
{
    static float vertices[] = {
        .4f, .5f, 0f,
        0f, -.5f, 0f,
        -.4f, .5f, 0f,
        -.15f, .25f, 0f,
        .15f, .25f, 0f,
    };
    FloatBuffer v_buffer;
    PointF origin;
    PointF accel;


    public Ship(float width)
    {
        v_buffer = floatBuffer(vertices, width);
        origin = new PointF(0f,0f);
        accel = new PointF(0f,0f);
    }

    public void draw(GL10 gl)
    {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, v_buffer);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, vertices.length / 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }




    public static FloatBuffer floatBuffer(float[] array, float width)
    {
        ByteBuffer byte_buf = ByteBuffer.allocateDirect(array.length * 4);
        byte_buf.order(ByteOrder.nativeOrder());
        FloatBuffer ret = byte_buf.asFloatBuffer();
        int i;
        for (i=0; i < array.length; i++) {
            ret.put(array[i]*width);
        }
        ret.position(0);
        return ret;
    }

}
