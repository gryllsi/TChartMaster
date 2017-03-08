package com.totcy.tchartlibrary.entity;

import java.util.List;

/**
 * Description 折线图 实体类
 * Author: tu
 * Date: 2016-11-24
 * Time: 11:47
 */
public class LineData {

    /** xlables 1、2、3、4 月份 ……*/
    private List<String> lables;
    /** chart data */
    private List<LineDataSet> dataSet;

    public LineData() {
    }

    public LineData(List<String> lables, List<LineDataSet> dataSet) {
        this.lables = lables;
        this.dataSet = dataSet;
    }

    public List<String> getLables() {
        return lables;
    }

    public void setLables(List<String> lables) {
        this.lables = lables;
    }

    public List<LineDataSet> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<LineDataSet> dataSet) {
        this.dataSet = dataSet;
    }
}
