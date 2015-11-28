package com.jt.circles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity
{

    private float x, y;
    private float ix = 0, iy = 0;
    private TextView tv;
    private boolean ini = false;

    private GestureDetectorCompat mDetector;
    private BubbleSurfaceView surfaceView;

    /**
     * Activity things
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new BubbleSurfaceView(this);
        setContentView(surfaceView);
        //setContentView(R.layout.activity_game);
        tv = (TextView) findViewById(R.id.display);

        // gesture stuff
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).registerListener(
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    //set ball speed based on phone tilt (ignore Z axis)
                    if (ini) calibrate(event);
                    x = -event.values[0] - ix;
                    y = event.values[1] - iy;
                    //timer event will redraw ball
                    BubbleSurfaceView.setAccel(x, y);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                } //ignore
            },
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE))
                    .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
            SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    /*@Override
    public void onPause()
    {
        //surfaceView.pauseThread();
        super.onPause();
    }*/

    public void calibrate(SensorEvent event) {
        ix = -event.values[0];
        iy = event.values[1];
        ini = false;
    }

    /**
     * Gesture detector things
     */

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return mDetector.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event)
        {
            BubbleSurfaceView.setShoot(true);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event)
        {
            ini = true;
        }
    }
}

    /*@Override
    protected void onResume()
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }*/

