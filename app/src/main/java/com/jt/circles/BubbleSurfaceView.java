package com.jt.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Jerry on 2015-11-28.
 */
public class BubbleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context ctx = null;
    private SurfaceHolder sh;
    private BubbleThread thread;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static float acx = 0, acy = 0;
    private static boolean shoot = false;
    private int time = 0;

    public BubbleSurfaceView(Context context) {
        super(context);
        sh = getHolder();
        sh.addCallback(this);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.WHITE);
        paint1.setStyle(Paint.Style.FILL);
        ctx = context;
        setFocusable(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread = new BubbleThread(sh, ctx, new Handler());
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public static void setAccel(float x, float y)
    {
        acx = x;
        acy = y;
    }

    public static void setShoot(boolean s)
    {
        shoot = s;
    }

    public void pauseThread()
    {
        try {

            sh.lockCanvas(null);
            synchronized (sh) {
                thread.wait();
            }
        }
        catch(InterruptedException e){}
    }

    public BubbleThread getThread()
    {
        return thread;
    }

    class BubbleThread extends Thread {
        private int canvasWidth = 900;
        private int canvasHeight = 1600;
        private static final int SPRITE_SPEED = 4; // for the sprite
        private static final float MAX_SPEED = 4;
        private boolean run = false;

        private float spriteX;
        private float spriteY;
        private float lastx = 0;
        private float lasty = 0;

        private float xdot = 0;
        private float ydot = 40;

        public BubbleThread(SurfaceHolder surfaceHolder, Context context,
                            Handler handler) {
            sh = surfaceHolder;
            handler = handler;
            ctx = context;
        }
        public void doStart() {
            synchronized (sh) {
                // Start bubble in centre and create some random motion
                spriteX = canvasWidth / 2;
                spriteY = canvasHeight / 2;
            }
        }
        public void run() {
            while (run) {
                Canvas c = null;
                try {
                    c = sh.lockCanvas(null);
                    synchronized (sh) {
                        doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        sh.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            run = b;
        }
        public void setSurfaceSize(int width, int height) {
            synchronized (sh) {
                canvasWidth = width;
                canvasHeight = height;
                doStart();
            }
        }
        private void doDraw(Canvas canvas) {

            ////////////////////////////////////////
            // move the sprite
            // limit total speed to 2
            float a = 1;
            if (Math.sqrt(acx * acx + acy * acy) > MAX_SPEED)
            {
                a = (float) (MAX_SPEED / Math.sqrt(acx * acx + acy * acy));
            }
            // x and y accel
            acx = a * acx;
            acy = a * acy;

            if (Math.abs(acx) > 0.3)
            {
                //if (Math.abs(acx) > 2) acx = 2 * acx/Math.abs(acx);
                spriteX += SPRITE_SPEED * acx;
            }
            if (Math.abs(acy) > 0.3)
            {
                //if (Math.abs(acy) > 2) acy = 2 * acy/Math.abs(acy);
                spriteY += SPRITE_SPEED * acy;
            }
            //dots
            if (time >= 2) {
                float a1 = 1;
                a1 = (float) (40.0 / Math.sqrt(acx * acx + acy * acy));
                if (Math.abs(acx) > 0.3 || Math.abs(acy) > 0.3) {
                    xdot = (int) (acx * a1);
                    ydot = (int) (acy * a1);
                }
                time = 0;
            }
            lastx = acx;
            lasty = acy;
            // end moving the sprite
            ///////////////////////////////////////////


            // boundaries for the sprite
            if (spriteX <= 50) spriteX = 50;
            if (spriteX >= canvasWidth - 50) spriteX = canvasWidth - 50;
            if (spriteY <= 50) spriteY = 50;
            if (spriteY >= canvasHeight - 50) spriteY = canvasHeight - 50;


            try {
                canvas.save();
                canvas.restore();
                // draw background
                canvas.drawColor(Color.WHITE);
                // sprite draws below
                canvas.drawCircle(spriteX, spriteY, 50, paint);
                canvas.drawCircle(spriteX + xdot, spriteY + ydot, 8, paint1);
            }
            catch(NullPointerException e)
            {}
            time++;
        }
    }
}
