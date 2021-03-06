package com.hlidskialf.android.gameshell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.pm.PackageManager;

public class DemoActivity extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        PackageManager pm = getPackageManager();
        boolean has_multi = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
        Log.v("Gamepad", "has distinct multitouch? "+has_multi);

        mRenderView = (DemoShipRenderView)findViewById(R.id.renderer);
        mRenderView.start();
        GamepadOverlayView mGamepad = (GamepadOverlayView)findViewById(R.id.gamepad);
        mGamepad.setOnJoystickListener(mRenderView);
        mGamepad.setOnButtonsListener(mRenderView);
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
