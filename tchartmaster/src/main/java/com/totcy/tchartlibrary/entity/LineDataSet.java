package com.totcy.tchartlibrary.entity;

import java.util.List;

/**
 * Description 折线图数据/画笔属性/
 * Author: tu
 * Date: 2016-11-24
 * Time: 14:48
 */
public class LineDataSet {

    /** data */
    private List<Entry> yVals;
    //draw line color
    private int lineColor;
    //draw online dot cloor
    private int dotColor;


    public List<Entry> getyVals() {
        return yVals;
    }

    public void setyVals(List<Entry> yVals) {
        this.yVals = yVals;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getDotColor() {
        return dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }
}
