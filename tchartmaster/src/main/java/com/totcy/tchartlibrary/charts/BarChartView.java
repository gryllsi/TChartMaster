package com.totcy.tchartlibrary.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.totcy.tchartlibrary.BaseChartView;
import com.totcy.tchartlibrary.R;
import com.totcy.tchartlibrary.entity.Entry;
import com.totcy.tchartlibrary.entity.LineData;
import com.totcy.tchartlibrary.entity.LineDataSet;
import com.totcy.tchartlibrary.utils.Utils;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * Description 条形图
 * Author: tu
 * Date: 2017-03-09
 * Time: 14:03
 */

public class BarChartView extends BaseChartView {

    private int yCount = 6; //y坐标刻度个数
    private float xScaleWidth = 20;//横坐标刻度间隔
    private int xTabHeight = 20; //x坐标标签高度
    private float maxData = 0;//坐标轴最大值

    private int yScaleHeight = Utils.dp2px(getContext(), 32);//纵坐标刻度间隔
    private float BarWidth = Utils.dp2px(getContext(), 14);//条形图的宽度
    private LineData mLineData;//数据源

    /**
     * 交点上的值
     */
    private boolean isShowValue = true;
    private float percent = 1;

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * init
     */
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TotcyChart, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.TotcyChart_TextSize) {
                axisSize = (int) a.getDimension(attr, axisSize);

            } else if (attr == R.styleable.TotcyChart_YscaleHeight) {
                yScaleHeight = (int) a.getDimension(attr, yScaleHeight);

            } else if (attr == R.styleable.TotcyChart_ChartPandding) {
                padding = a.getDimension(attr, padding);

            } else if (attr == R.styleable.TotcyChart_ChartLineWidth) {
                lineStrokeWidth = a.getDimension(attr, lineStrokeWidth);

            } else if (attr == R.styleable.TotcyChart_BarWidth) {
                BarWidth = a.getDimension(attr, BarWidth);

            } else {
            }
        }
        a.recycle();

        initPaint();

        xTabHeight = (int) xYAxisPaint.measureText("11");
        //初始化原点y
        //圆点Y轴坐标  上面padding +xTabHeight +（yCount-1个y轴间隔）
        dotY = padding + xTabHeight + (yCount - 1) * yScaleHeight;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int height;
        int width;
        //宽度测量
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Utils.getScreenWidth(getContext());
        }
        chartViewWidth = width;

        //圆点X轴坐标  最大值的长度
        dotX = padding + xYAxisPaint.measureText(maxData + "") + marginXy;
        //高度测量 上面padding + （yCount-1个y轴间隔） + marginXy +xTabHeight + padding
        chartViewHigth = (int) (dotY + 2 * marginXy + xTabHeight + padding);
        //计算出折线点之间的间隔
        // TODO: 2017/3/9 这里计算需要更改
        //if (mLineData != null)
        //xScaleWidth = (chartViewWidth - dotX - padding) / mLineData.getLables().size();
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = chartViewHigth;
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 绘制纵坐标以及纵坐标横线
     */
    private void drawOrdinate(Canvas canvas) {
        xYAxisPaint.setTextSize(axisSize);
        xYAxisPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < yCount; i++) {
            xYAxisPaint.setColor(axisColor);
            //横线
            canvas.drawLine(dotX, dotY - i * yScaleHeight, chartViewWidth - padding, dotY - i * yScaleHeight, xYAxisPaint);
            //纵坐标文字
            xYAxisPaint.setColor(textColor);
            String xNum = new BigDecimal(maxData)
                    .divide(new BigDecimal(yCount - 1), 0, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(i)).toString();
            canvas.drawText(xNum, padding, dotY - i * yScaleHeight, xYAxisPaint);
        }
        xYAxisPaint.setColor(axisColor);
        //竖线
        canvas.drawLine(dotX, padding + xTabHeight - 15, dotX, dotY, xYAxisPaint);
    }

    /**
     * 绘制xtab和 图
     */
    private void drawAbscissaAndChart(Canvas canvas) {
        if (mLineData == null)
            return;
        xYAxisPaint.setColor(textColor);
        xYAxisPaint.setTextSize(axisSize);
        //条形图组总数
        int barSize = mLineData.getDataSet().size();
        for (int i = 0; i < mLineData.getLables().size(); i++) {
            String xLable = mLineData.getLables().get(i);
            xYAxisPaint.setTextAlign(Paint.Align.CENTER);
            //横坐标文字
            canvas.save();
            canvas.drawText(xLable, dotX + (barSize * i + (float) barSize / 2) * BarWidth + i * xScaleWidth, dotY + marginXy + xTabHeight, xYAxisPaint);
            canvas.restore();
        }
        Rect rect = new Rect();//Rect对象
        //多组条形图
        for (int i = 0; i < barSize; i++) {
            //设置每组条形图的颜色
            LineDataSet dataSet = mLineData.getDataSet().get(i);
            chartPaint.setColor(dataSet.getLineColor());
            //绘制每组条形图
            for (int j = 0; j < dataSet.getyVals().size(); j++) {
                Entry entry = dataSet.getyVals().get(j);
                rect.top = (int) getBarHeight(entry.getVal());
                rect.bottom = (int) dotY;
                rect.left = (int) (dotX + j * xScaleWidth + (barSize * j + i) * BarWidth);
                rect.right = (int) (rect.left + BarWidth);
                canvas.drawRect(rect, chartPaint);
                //绘制数值
                if (isShowValue) {
                    //绘制折线点上的值
                    xYAxisPaint.setTextSize(axisSize * 2 / 3);
                    xYAxisPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText((int) entry.getVal() + "", rect.right - BarWidth / 2, getBarHeight(entry.getVal()) - marginXy, xYAxisPaint);
                }

            }
        }


    }

    /**
     * 获得折线图点的高度
     *
     * @return
     */
    private float getBarHeight(float amt) {
        float result = (new BigDecimal(1).subtract(new BigDecimal(amt).divide(new BigDecimal(maxData), 2, BigDecimal.ROUND_HALF_UP)))
                .multiply(new BigDecimal((yCount - 1) * yScaleHeight)).add(new BigDecimal(padding + xTabHeight)).floatValue();
        return result < dotY ? result : dotY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOrdinate(canvas);
        drawAbscissaAndChart(canvas);
    }

    /**
     * 初始化数据
     *
     * @param data
     */
    private void initData(LineData data) {

        this.mLineData = data;
        //求出所有折线数据的最大值 作为纵坐标的最高点
        float max = maxData;
        for (LineDataSet dataSet : mLineData.getDataSet()) {
            float temp = Collections.max(dataSet.getyVals()).getVal();
            if (temp > max) {
                max = temp;
            }
        }
        maxData = max;

        //圆点X轴坐标  最大值的长度 xYAxisPaint.measureText(CommonUtils.dealMoney(new BigDecimal(maxData),0,"#,##0"))
        dotX = padding + xYAxisPaint.measureText(maxData + "") + marginXy;
        //计算出折线点之间的间隔
        //xScaleWidth = (chartViewWidth - dotX - padding) / mLineData.getLables().size();
    }

    /**
     * 设置图表数据源
     *
     * @param data
     */
    public void setLineData(LineData data) {
        initData(data);
        this.invalidate();
    }

    public boolean isShowValue() {
        return isShowValue;
    }

    public void setShowValue(boolean showValue) {
        isShowValue = showValue;
        this.invalidate();
    }

}
