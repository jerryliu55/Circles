package com.jt.circles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private float x, y, z;
    private float ix, iy, iz;
    private TextView tv;
    private boolean ini = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new BubbleSurfaceView(this));
        //setContentView(R.layout.activity_game);
        tv = (TextView) findViewById(R.id.display);

        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    //set ball speed based on phone tilt (ignore Z axis)
                    if (ini)
                    {
                        ix = -event.values[0];
                        iy = event.values[1];
                        iz = event.values[2];
                        ini = false;
                    }
                    x = -event.values[0] - ix;
                    y = event.values[1] - iy;
                    z = event.values[2] - iz;
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

    /*@Override
    protected void onResume()
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }*/
}
