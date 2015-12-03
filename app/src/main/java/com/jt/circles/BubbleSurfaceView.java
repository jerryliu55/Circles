package com.jt.circles;
//all imports
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private static boolean lose = false;
    private int time = 0;
    private int loop = 0;
    private int last = 0;
    public int score = 0;
    private Paint whit = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context callingContext;
    public boolean over = false;

    public BubbleSurfaceView(Context context) {
        //calls surfaceView context
        super(context);
        callingContext = context;
        //initialize sh, from surfaceView method
        sh = getHolder();
        sh.addCallback(this);
        //set colour based on parameter

        whit.setColor(Color.WHITE);
        whit.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 8; i++)
        {
            paint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint[i].setStyle(Paint.Style.FILL);
        }
        paint[0].setColor(Color.rgb(135,206,250));
        paint[1].setColor(Color.rgb(65,105,225)); // Royal Blue
        paint[2].setColor(Color.rgb(34, 139, 34)); //forest Green
        paint[3].setColor(Color.rgb(186, 85, 211));
        paint[4].setColor(Color.rgb(152, 251, 152)); //pale green
        paint[5].setColor(Color.rgb(220, 20, 60));//light red
        paint[6].setColor(Color.rgb(255, 165, 0));

        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(100);
        paintText.setTypeface(GameActivity.quicksand);


        for (int i = 0; i < 30; i++)
        {

            c[i] = new Circle((int)(Math.random()*6.999), 1900/2, 1080/2);
            c[i].setStat(0);
            c[i].setCX(60 + i * 201);
            c[i].setHX(i);
            c[i].setHY(4);
        }
        control = 0;
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
    public boolean getOver()
    {
        return over;
    }
    public int getScore()
    {
        return score;
    }


    class BubbleThread extends Thread {
        private int canvasWidth = 1900;
        private int canvasHeight = 1080;
        private static final int SPEED = 2;
        private static final int SPRITE_SPEED = 4; // for the sprite
        private static final float MAX_SPEED = 4;
        private boolean run = false;

        private float lastx = 0;
        private float lasty = 0;

        private float xdot = 0;
        private float ydot = 40;
        private int thirty = 0;

        private float spriteX;
        private float spriteY;
        private float headingX;
        private float headingY;

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
                int a;
                try {
                    c = sh.lockCanvas(null);
                    synchronized (sh) {
                        a = doDraw(c);
                        if (a == 1) {

                            return;
                        }
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
        public boolean getRunning()
        {
            return run;
        }
        public void setSurfaceSize(int width, int height) {
            synchronized (sh) {
                canvasWidth = width;
                canvasHeight = height;
                doStart();
            }
        }
        private int doDraw(Canvas canvas) {
            ////////////////////////////////////////
            // move the sprite
            // limit total speed to 2
            float a = 1;

            if (shoot == true)
            {
                setShoot(false);
                int next = 0;
                int cxx = c[control].getCX();
                int cyy = c[control].getCY();
                last = control;
                double xa = acx;
                double xb = acy;
                if (xa == 0 && xb == 0)
                    return 0;
                //if (xb == 0)
                //xb = (10*Math.random());
                double multiplier = (MAX_SPEED)/Math.sqrt(acx*acx + acy*acy);
                double m2 = 110/Math.sqrt(acx*acx + acy*acy);
                c[control].setHX((int)(SPRITE_SPEED*multiplier*xa));
                c[control].setHY((int)(SPRITE_SPEED*multiplier*xb));
                if (control + 1 < 30 && thirty != 1)
                    next = control + 1;
                else
                {
                    thirty  = 1;
                    yo:for(int i = 0; i < 30; i++)
                    {
                        if (c[i].getStat() == 0) {
                            next = i;
                            c[next] = new Circle ((int)(Math.random()*6.99), 1900/2, 1080/2);
                            c[next].setStat(1);
                            break yo;
                        }
                    }
                }
                if (c[control].getHX()>0) {
                    c[control].setCX((int)(cxx+xa*m2+30));
                    c[next].setCX(cxx);
                }
                else
                {
                    c[control].setCX((int)(cxx + xa*m2-30));
                    c[next].setCX(cxx);
                }
                if (c[control].getHY()>0) {
                    c[control].setCY((int)(cyy+xb*m2+30));
                    c[next].setCY(cyy);
                }
                else
                {
                    c[control].setCY((int)(cyy + xb*m2-30));
                    c[next].setCY(cyy);
                }
                loop = 0;
                control = next;
                c[control].setStat(1);
            }

            if (loop == 10) {
                double rand = Math.random();
                c[last].setHX((int)((0.70)*c[last].getHX()));
                c[last].setHY((int)((0.70)*c[last].getHY()));
            }


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
                spriteX += (SPRITE_SPEED) * acx;
            }
            if (Math.abs(acy) > 0.3)
            {
                //if (Math.abs(acy) > 2) acy = 2 * acy/Math.abs(acy);
                spriteY += (SPRITE_SPEED) * acy;
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

            if (spriteX <= 50) spriteX = 50;
            if (spriteX >= canvasWidth - 50) spriteX = canvasWidth - 50;
            if (spriteY <= 50) spriteY = 50;
            if (spriteY >= canvasHeight - 50) spriteY = canvasHeight - 50;

            c[control].setCX((int) spriteX);
            c[control].setCY((int)spriteY);


            int x = 0;
            for (int i = 0;i < 30; i++)
            {
                for (int j = i+1; j < 30; j++)
                {
                    if (c[i].getStat() == 1 && c[j].getStat() == 1 ) {
                        if (i == control || j == control) {
                            x = Circle.collision(c[i], c[j], 1);

                            if (x == 1) {
                            /*Intent endIntent = new Intent(callingContext, EndActivity.class);
                            endIntent.putExtra("score", String.valueOf(score));*/
                                over = true;
                                //return 1;
                            }
                            if (x == 2) {
                                score += 2;
                                c[control].setStat(1);
                                c[control].setColour((int) (Math.random() * 6.99));
                            }
                        }
                        else {
                            score += Circle.collision(c[i], c[j], 0);
                        }
                    }
                }
            }
            for (int i = 0; i < 30; i++) //need to change to numCirclesActive
            {
                if (i == control)
                {
                    /*if (c[i].getCX() >= canvasWidth - 50) c[control].setCX(canvasWidth-50);
                    if (c[i].getCX() <= 50) c[control].setCX(50);
                    if (c[i].getCY() >= canvasHeight - 50) c[control].setCX(canvasHeight-50);
                    if (c[i].getCY() <= 50) c[control].setCY(50);*/
                }
                else {
                    if (c[i].getCX() >= canvasWidth - 50){
                        c[i].setCX(canvasWidth - 50);
                        c[i].setHX(-1 * c[i].getHX());
                    }
                    if (c[i].getCX() <= 50)
                    {
                        c[i].setCX(50);
                        c[i].setHX(-1 * c[i].getHX());
                    }
                    if (c[i].getCY() >= canvasHeight - 50) {
                        c[i].setCY(canvasHeight-50);
                        c[i].setHY(-1 * c[i].getHY());
                    }
                    if (c[i].getCY() <= 50)
                    {
                        c[i].setCY(50);
                        c[i].setHY(-1 * c[i].getHY());
                    }
                }
            }
            try {
                canvas.save();
                canvas.restore();
                canvas.drawColor(Color.WHITE);
                for (int i = 0; i < 30; i++) {
                    if (c[i].getStat() == 1) {
                        canvas.drawCircle(c[i].getCX(), c[i].getCY(), 50, paint[c[i].getColor()]);
                        if (i == control) {
                            //canvas.drawCircle(c[control].getCX() + xdot, c[control].getCY() + ydot, 8, whit);
                            canvas.drawCircle(c[control].getCX(), c[control].getCY(), 20, whit);
                        }
                        if (i != control)
                            c[i].inc();
                    }
                }
                canvas.drawText(String.valueOf(score), canvasWidth - 145, 120, paintText);
            }catch(NullPointerException e) {} catch(ArrayIndexOutOfBoundsException e){}
            if (over) return 1;
            time++;
            loop++;
            return 0;
        }

    }
}