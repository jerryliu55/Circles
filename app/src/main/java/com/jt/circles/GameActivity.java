package com.jt.circles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity
{
    public static Typeface quicksand;

    private float x, y;
    private static float ix = 0, iy = 0;
    private TextView tv;
    private boolean ini = true;
    private AsyncCheck check;

    private GestureDetectorCompat mDetector;
    private BubbleSurfaceView surfaceView;

    private MyGestureListener gestureListener = new MyGestureListener();

    /**
     * Activity things
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quicksand = Typeface.createFromAsset(getAssets(), "Quicksand-Bold.ttf");

        surfaceView = new BubbleSurfaceView(this);
        setContentView(surfaceView);

        //setContentView(R.layout.activity_game);
        //tv = (TextView) findViewById(R.id.display);

        // gesture stuff
        mDetector = new GestureDetectorCompat(this, gestureListener);

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

        check = new AsyncCheck(this);
        check.execute();
        /*while (!(surfaceView.getOver()))
        {
            score = surfaceView.getScore();
        }*/
        /*final Intent endIntent = new Intent(this, EndActivity.class);
        Thread checkOver = new Thread(new Runnable() {
            public void run() {
                if (surfaceView.getOver())
                {
                    finish();
                    endIntent.putExtra("score", String.valueOf(surfaceView.getScore()));
                    Log.d("a", "got here");
                    startActivity(endIntent);
                }
                Log.d("a", "checking");
            }
        });
        checkOver.start();*/

        /*Intent endIntent = new Intent(this, EndActivity.class);
        endIntent.putExtra("score", String.valueOf(score));*/
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //check.cancel(true);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            System.exit(1);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void calibrate(SensorEvent event) {
        ix = -event.values[0];
        iy = event.values[1];

        // make toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setTypeface(quicksand);
        text.setTextSize(15);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        ini = false;
    }

    private class AsyncCheck extends AsyncTask<String, Void, String>
    {
        Context context;
        private AsyncCheck(Context context)
        {
            this.context = context.getApplicationContext();
        }
        @Override
        protected String doInBackground(String... params)
        {

            while (!(surfaceView.over)) {
                //Log.d("b", "bbbbbb");

            }
            //Log.d("a", "aaaa");
            return String.valueOf(surfaceView.score);
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            //Log.d("c", "cccccc");
            if (!(result.equals(""))) {
                //Log.d("d", "ddddd");
                Intent endIntent = new Intent(context, EndActivity.class);
                endIntent.putExtra("score", result);
                startActivity(endIntent);
            }
        }
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

        private MotionEvent active;
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        /*@Override
        public boolean onSingleTapConfirmed(MotionEvent event)
        {
            BubbleSurfaceView.setShoot(true);
            return true;
        }*/

        @Override
        public boolean onSingleTapUp(MotionEvent event)
        {
            BubbleSurfaceView.setShoot(true);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event)
        {
            active = event;
            ini = true;
        }
    }
}