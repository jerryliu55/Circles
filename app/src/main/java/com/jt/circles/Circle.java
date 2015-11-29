package com.jt.circles;

import android.graphics.Color;

/**
 * Created by Tony on 2015-11-28.
 */
public class Circle {
    private int headingX;
    private int headingY;
    private int coordX;
    private int coordY;
    private int color;
    private int stat;

    public Circle(int color, int x, int y){
        headingX = 0;
        headingY = 0;
        coordX = x;
        coordY = y;
        stat = 0;
        this.color = color;
    }

    public void setHX(int v)
    {
        headingX = v;
    }

    public int getStat()
    {
        return stat;
    }

    public void setStat(int x)
    {
        stat = x;
    }
    public void setHY(int v)
    {
        headingY = v;
    }

    public void setCY(int v)
    {
        coordY = v;
    }

    public void setCX(int v)
    {
        coordX = v;
    }

    public int getHX()
    {
        return headingX;
    }

    public int getHY()
    {
        return headingY;
    }

    public int getCX()
    {
        return coordX;
    }

    public int getCY()
    {
        return coordY;
    }

    public int getColor()
    {
        return color;
    }

    public void setColour(int col)
    {
        color = col;
    }
    public void resp()
    {
        headingX = 0;
        headingY = 0;
        coordX = 100;
        coordY = 100;
        color = (int)(Math.random()*6.99);
        stat = 0;
    }

    public void remove()
    {
        this.color = Color.WHITE;
        this.stat = 0;
    }

    public static int equals(Circle x, Circle y)
    {
        if (x == null || y == null)
            return 0;
        if (x.getColor() == y.getColor())
        {
            return 1;
        }
        return 0;
    }

    public static int collision(Circle x, Circle y, int control)
    {
        if (Math.sqrt((Math.pow((x.getCX()-y.getCX()), 2)+Math.pow((x.getCY()-y.getCY()), 2.0))) <= 95)
        {

            if (control == 1 && equals(x, y) == 1)
            {
                x.remove();
                y.remove();
                return 2;
            }
            else if (equals(x,y) == 1) {
                x.remove();
                y.remove();
                return 3;
            }
            else if (control == 1){
                return 1;
            }

            if (x.headingY * y.headingY <= 0) {
                y.headingY *= -1;
                x.headingY *= -1;
            }
            else if (x.getCY() > y.getCY() && x.headingY > 0)
                y.headingY*=-1;
            else if (x.getCY() > y.getCY() && x.headingY < 0)
                x.headingY*=-1;
            else if (y.getCY() > x.getCY() && x.headingY > 0)
                x.headingY*=-1;
            else if (y.getCY() > x.getCY() && x.headingY < 0)
                y.headingY*=-1;
            if (x.headingX * y.headingX <= 0) {
                y.headingX *= -1;
                x.headingX *= -1;
            }
            else if (x.getCX() > y.getCX() && x.headingX > 0)
                y.headingX*=-1;
            else if (x.getCX() > y.getCX() && x.headingX < 0)
                x.headingX*=-1;
            else if (y.getCX() > x.getCX() && x.headingX > 0)
                x.headingX*=-1;
            else if (y.getCX() > x.getCX() && x.headingX < 0)
                y.headingX*=-1;
            else {
                x.headingX *=-1;
                x.headingY *=-1;
            }
            x.inc();
            y.inc();
            return 0;
        }
        return 0;
    }
    public void inc()
    {
        coordX = coordX + headingX;
        coordY = coordY + headingY;
    }

}