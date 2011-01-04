package com.hlidskialf.android.gameshell;

import android.app.Activity;
import android.os.Bundle;

public class DemoActivity extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mRenderView = (DemoShipRenderView)findViewById(R.id.renderer);
        mRenderView.start();
        GamepadOverlayView mGamepad = (GamepadOverlayView)findViewById(R.id.gamepad);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mRenderView.onPause();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mRenderView.onResume();
    }



    /* private */
    DemoShipRenderView mRenderView;
}
