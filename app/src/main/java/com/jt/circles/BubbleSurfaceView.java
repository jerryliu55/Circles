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
    private final Paint paintSprite = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static float acx = 0, acy = 0;
    private static boolean shoot = false;

    public BubbleSurfaceView(Context context) {
        super(context);
        sh = getHolder();
        sh.addCallback(this);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paintSprite.setColor(Color.GREEN);
        paintSprite.setStyle(Paint.Style.FILL);
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

    public BubbleThread getThread()
    {
        return thread;
    }

    class BubbleThread extends Thread {
        private int canvasWidth = 200;
        private int canvasHeight = 400;
        private static final int SPEED = 8;
        private static final int SPRITE_SPEED = 4;
        private static final float MAX_SPEED = 4;
        private boolean run = false;

        private float bubbleX;
        private float bubbleY;
        private float headingX;
        private float headingY;

        private float spriteX;
        private float spriteY;

        public BubbleThread(SurfaceHolder surfaceHolder, Context context,
                            Handler handler) {
            sh = surfaceHolder;
            handler = handler;
            ctx = context;
        }
        public void doStart() {
            synchronized (sh) {
                // Start bubble in centre and create some random motion
                bubbleX = canvasWidth / 2;
                bubbleY = canvasHeight / 2;
                spriteX = canvasWidth / 2;
                spriteY = canvasHeight / 2;
                headingX = (float) (-1 + (Math.random() * 2));
                headingY = (float) (-1 + (Math.random() * 2));
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
            if (bubbleX <= 50 || bubbleX >= canvasWidth - 50) headingX *= -1;
            if (bubbleY <= 50 || bubbleY >= canvasHeight - 50) headingY *= -1;
            bubbleX = bubbleX + (headingX * SPEED);
            bubbleY = bubbleY + (headingY * SPEED);
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
            if (Math.abs(acx) > 0.7)
            {
                //if (Math.abs(acx) > 2) acx = 2 * acx/Math.abs(acx);
                spriteX += SPRITE_SPEED * acx;
            }
            if (Math.abs(acy) > 0.7)
            {
                //if (Math.abs(acy) > 2) acy = 2 * acy/Math.abs(acy);
                spriteY += SPRITE_SPEED * acy;
            }
            if (spriteX <= 50) spriteX = 50;
            if (spriteX >= canvasWidth - 50) spriteX = canvasWidth - 50;
            if (spriteY <= 50) spriteY = 50;
            if (spriteY >= canvasHeight - 50) spriteY = canvasHeight - 50;

            /*canvas.save();
            canvas.restore();*/
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(bubbleX, bubbleY, 50, paint);
            canvas.drawCircle(spriteX, spriteY, 50, paint);
        }
    }
}
