package com.totcy.tchartlibrary.charts;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.totcy.tchartlibrary.BaseChartView;
import com.totcy.tchartlibrary.R;
import com.totcy.tchartlibrary.entity.Entry;
import com.totcy.tchartlibrary.entity.LineData;
import com.totcy.tchartlibrary.entity.LineDataSet;
import com.totcy.tchartlibrary.utils.Utils;

import java.math.BigDecimal;
import java.util.Collections;


/**
 * Description 折线图
 * Author: tu
 * Date: 2016-11-24
 * Time: 11:56
 */
public class LineChartView extends BaseChartView {

    private static final String TAG = "LineChartView_Tag";

    private int yCount = 6; //y坐标刻度个数
    private float xScaleWidth = 20;//横坐标刻度间隔
    private int xTabHeight = 20; //x坐标标签高度
    private float maxData = 0;//坐标轴最大值

    private int yScaleHeight = Utils.dp2px(getContext(), 32);//纵坐标刻度间隔
    private LineData mLineData;//数据源
    /**
     * 是否绘制平滑的曲线
     */
    private boolean isSmooth = false;
    /**
     * 绘制折线交点（isSmooth为false时有效）
     */
    private boolean isIntersection = true;
    /**
     * 交点上的值
     */
    private boolean isShowValue = false;
    private float percent = 1;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
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

            } else {
            }
        }
        a.recycle();

        initPaint();

        xTabHeight = (int) xYAxisPaint.measureText("11");
        //初始化原点y
        //圆点Y轴坐标  上面padding +xTabHeight +（yCount-1个y轴间隔）
        dotY = padding + xTabHeight + (yCount - 1) * yScaleHeight;

        Log.d(TAG, "init: ");
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
        if (mLineData != null)
            xScaleWidth = (chartViewWidth - dotX - padding) / mLineData.getLables().size();
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = chartViewHigth;
        }
        setMeasuredDimension(width, height);
        Log.d(TAG, "onMeasure: ");
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
     * 绘制xtab和折线图
     */
    private void drawAbscissaAndChart(Canvas canvas) {
        if (mLineData == null)
            return;

        xYAxisPaint.setColor(textColor);
        xYAxisPaint.setTextSize(axisSize);

        for (int i = 0; i < mLineData.getLables().size(); i++) {
            String xLable = mLineData.getLables().get(i);
            xYAxisPaint.setTextAlign(Paint.Align.LEFT);
            //横坐标文字
            canvas.save();
            canvas.drawText(xLable, dotX + i * xScaleWidth, dotY + marginXy + xTabHeight, xYAxisPaint);
            canvas.restore();
        }
        //多条折线
        for (LineDataSet dataSet : mLineData.getDataSet()) {
            Path path = new Path();
            path.moveTo(dotX, getBarHeight(dataSet.getyVals().get(0).getVal()));

            int size = dataSet.getyVals().size();
            //绘制折线
            for (int i = 1; i < size; i++) {
                Entry entry = dataSet.getyVals().get(i);
                //折线 path路径的点
                path.lineTo(dotX + i * xScaleWidth * percent, getBarHeight(entry.getVal()));
            }
            //平滑曲线设置
            if (isSmooth) {
                CornerPathEffect cornerPathEffect = new CornerPathEffect(10);
                chartPaint.setPathEffect(cornerPathEffect);
            } else {
                CornerPathEffect cornerPathEffect = new CornerPathEffect(0);
                chartPaint.setPathEffect(cornerPathEffect);
            }
            //空心
            chartPaint.setStyle(Paint.Style.STROKE);
            chartPaint.setColor(dataSet.getLineColor());
            canvas.drawPath(path, chartPaint);

            //绘制折线的圆点和每个点的的值（不在上一个循环内写是因为这些点和值要在折线的上面显示）
            if (isShowValue || (!isSmooth && isIntersection))
                for (int i = 0; i < size; i++) {
                    Entry entry = dataSet.getyVals().get(i);
                    //绘制折线的圆点 不能是圆滑的曲线
                    if (!isSmooth && isIntersection) {
                        chartPaint.setStyle(Paint.Style.FILL);
                        chartPaint.setColor(dataSet.getDotColor());
                        canvas.drawCircle(dotX + i * xScaleWidth * percent, getBarHeight(entry.getVal()), lineStrokeWidth * 2, chartPaint);
                    }
                    if (isShowValue) {
                        //绘制折线点上的值
                        xYAxisPaint.setTextSize(axisSize * 2 / 3);
                        xYAxisPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText((int) entry.getVal() + "", dotX + i * xScaleWidth * percent, getBarHeight(entry.getVal()) - marginXy, xYAxisPaint);
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
     * @return 返回折线数据中最大的个数
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
        xScaleWidth = (chartViewWidth - dotX - padding) / mLineData.getLables().size();
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

    /**
     * 设置图表数据源 （带动画）
     *
     * @param data
     */
    public void setLineDataWithAnim(LineData data) {
        Log.d(TAG, "setLineDataWithAnim: ");
        initData(data);
        //ObjectAnimator o1 = ObjectAnimator.ofInt(this, "maxCount", 0, count);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(this, "percent", 0.1f, 1f);

        AnimatorSet set = new AnimatorSet();
        //set.play(o1).with(o2);
        set.play(o2);
        set.setDuration(1500);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
        //requestLayout();
    }

    public boolean isShowValue() {
        return isShowValue;
    }

    public void setShowValue(boolean showValue) {
        isShowValue = showValue;
        this.invalidate();
    }

    public boolean isSmooth() {
        return isSmooth;
    }

    public void setSmooth(boolean smooth) {
        isSmooth = smooth;
        //平滑的曲线时 不显示折线上圆点
        if (smooth) {
            isIntersection = false;
        }
        this.invalidate();
    }

    public boolean isIntersection() {
        return isIntersection;
    }

    public void setIntersection(boolean intersection) {
        isIntersection = intersection;
        this.invalidate();
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
        this.invalidate();
    }
}
