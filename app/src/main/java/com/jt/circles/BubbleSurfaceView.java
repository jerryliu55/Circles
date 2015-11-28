package com.jt.circles;
//all imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Jerry on 2015-11-28.
 * Edits by Tony.
 */
public class BubbleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context ctx = null;
    private SurfaceHolder sh;
    private BubbleThread thread;
    private static boolean shoot = false;
    private static float acx = 0, acy = 0;
    private final Paint paint[] = new Paint[9];
    private final Paint paintText = new Paint();
    Circle c[] = new Circle[30];
    private static int control = 0;
    private int time = 0;

    public BubbleSurfaceView(Context context) {
        //calls surfaceView context
        super(context);
        //initialize sh, from surfaceView method
        sh = getHolder();
        sh.addCallback(this);
        //set colour based on parameter
        for (int i = 0; i < 9; i++)
        {
            paint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint[i].setStyle(Paint.Style.FILL);
        }
        paint[0].setColor(Color.RED);
        paint[1].setColor(Color.CYAN);
        paint[2].setColor(Color.BLUE);
        paint[3].setColor(Color.GREEN);
        paint[4].setColor(Color.BLACK);
        paint[5].setColor(Color.DKGRAY);
        paint[6].setColor(Color.MAGENTA);
        paint[7].setColor(Color.YELLOW);
        paint[8].setColor(Color.LTGRAY);

        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(80);
        paintText.setTypeface(GameActivity.quicksand);

        for (int i = 0; i < 30; i++)
        {
            c[i] = new Circle(i%9);
            c[i].setCX(60 + i * 201);
            c[i].setHX(i);
            c[i].setHY(4);
        }
        c[0].setStat(1);
        ctx = context;
        setFocusable(true);//make sure to get key events
    }

    //create and start the thread
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new BubbleThread(sh, ctx, new Handler());
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);
    }

    //when surface destroyed, stop thread
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
    //returns thread
    public BubbleThread getThread()
    {
        return thread;
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


    class BubbleThread extends Thread {
        private int canvasWidth = 200;
        private int canvasHeight = 400;
        private static final int SPEED = 2;
        private static final int SPRITE_SPEED = 4; // for the sprite
        private static final float MAX_SPEED = 4;
        private boolean run = false;

        private float lastx = 0;
        private float lasty = 0;

        private float xdot = 0;
        private float ydot = 40;

        private float spriteX;
        private float spriteY;
        private float headingX;
        private float headingY;

        private int score = 0;

        public BubbleThread(SurfaceHolder surfaceHolder, Context context,
                            Handler handler) {
            sh = surfaceHolder;
            handler = handler;
            ctx = context;
        }
        public void doStart() {
            synchronized (sh) {
                // Start bubble in centre and create some random motion
                spriteX = canvasWidth / 2;//change these to location of start
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


            if (spriteX <= 50) spriteX = 50;
            if (spriteX >= canvasWidth - 50) spriteX = canvasWidth - 50;
            if (spriteY <= 50) spriteY = 50;
            if (spriteY >= canvasHeight - 50) spriteY = canvasHeight - 50;

            c[control].setCX((int)spriteX);
            c[control].setCY((int)spriteY);
            // end moving the sprite
            ///////////////////////////////////////////


            for (int i = 0; i <= control; i++) //need to change to numCirclesActive
            {
                if (i == control)
                {
                    /*if (c[i].getCX() >= canvasWidth - 50) c[control].setCX(canvasWidth-50);
                    if (c[i].getCX() <= 50) c[control].setCX(50);
                    if (c[i].getCY() >= canvasHeight - 50) c[control].setCX(canvasHeight-50);
                    if (c[i].getCY() <= 50) c[control].setCY(50);*/
                }
                else {
                    if (c[i].getCX() >= canvasWidth - 50 || c[i].getCX() <= 50) {
                        c[i].setHX(-1 * c[i].getHX());
                    }
                    if (c[i].getCY() >= canvasHeight - 50 || c[i].getCY() <= 50) {
                        c[i].setHY(-1 * c[i].getHY());
                    }
                }
            }

            for (int i = 0;i <= control; i++)
            {
                for (int j = i+1; j < 30; j++)
                {
                    if (c[i].getStat() == 1 && c[j].getStat() == 1 ) {
                        if (i == control || j == control)
                            score += 2 * Circle.collision(c[i], c[j], 1);
                        else
                            score += Circle.collision(c[i], c[j], 0);
                    }
                }
            }
            try {
                canvas.save();
                canvas.restore();
                canvas.drawColor(Color.WHITE);
                for (int i = 0; i <= control; i++) {
                    if (c[i].getStat() == 1) {
                        canvas.drawCircle(c[i].getCX(), c[i].getCY(), 50, paint[c[i].getColor()]);
                        if (i == control)
                            canvas.drawCircle(c[control].getCX() + xdot, c[control].getCY() + ydot, 8, paint[7]);
                        if (i != control)
                            c[i].inc();
                    }
                }
                canvas.drawText(String.valueOf(score), canvasWidth - 150, 80, paintText);
            }catch(NullPointerException e) {}
            time++;
        }
    }
}