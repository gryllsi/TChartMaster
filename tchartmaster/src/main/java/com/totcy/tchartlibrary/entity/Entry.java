package com.totcy.tchartlibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Description 图表数据值
 * Author: tu
 * Date: 2016-11-24
 * Time: 15:54
 */
public class Entry implements Parcelable ,Comparable<Entry>{

    /** the actual value */
    private float mVal = 0f;

    /** the index on the x-axis */
    private int mXIndex = 0;

    public float getVal() {
        return mVal;
    }

    public void setVal(float val) {
        mVal = val;
    }

    public int getXIndex() {
        return mXIndex;
    }

    public void setXIndex(int XIndex) {
        mXIndex = XIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mVal);
        dest.writeInt(this.mXIndex);
    }

    public Entry(float val, int XIndex) {
        mVal = val;
        mXIndex = XIndex;
    }

    public Entry() {
    }

    protected Entry(Parcel in) {
        this.mVal = in.readFloat();
        this.mXIndex = in.readInt();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    @Override
    public int compareTo(Entry o) {
        return new BigDecimal(this.getVal()).compareTo(new BigDecimal(o.getVal()));
    }
}
