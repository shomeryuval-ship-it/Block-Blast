package com.example.block_blast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell {

    boolean yes;
    int cx;
    int cy;
    int cw;
    int ch;

    public Cell(int cw, int ch, int cy, int cx,boolean yes) {
        this.cw = cw;
        this.ch = ch;
        this.cy = cy;
        this.cx = cx;
        this.yes = yes;
    }

    public boolean isYes() {
        return yes;
    }

    public void setYes(boolean yes) {
        this.yes = yes;
    }



    public void setCx(int cx) {
        this.cx = cx;
    }



    public void setCy(int cy) {
        this.cy = cy;
    }


    public void drawCell(Canvas canvas){
        Paint mypaint = new Paint();
        if (yes){
        mypaint.setARGB(255, 100, 100, 100);
        mypaint.setStyle(Paint.Style.FILL);}
        else{
            mypaint.setARGB(255, 220, 220, 220);
            mypaint.setStyle(Paint.Style.FILL);}


        canvas.drawRect(cx,cy,cx+cw,cy+ch,mypaint);
        mypaint.setARGB(255, 190, 190, 190);
        mypaint.setStyle(Paint.Style.STROKE);
        mypaint.setStrokeWidth(10);
        canvas.drawRect(cx,cy,cx+cw,cy+ch,mypaint);


    }

}
