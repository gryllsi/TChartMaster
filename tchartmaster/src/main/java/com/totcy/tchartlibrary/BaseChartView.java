package com.totcy.tchartlibrary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.totcy.tchartlibrary.utils.Utils;


/**
 * Description 图表库基类
 * Author: tu
 * Date: 2016-11-24
 * Time: 11:15
 */
public abstract class BaseChartView extends View {

    protected Paint xYAxisPaint;//xy轴画笔
    protected Paint chartPaint;//条形图画笔

    protected int axisColor = Color.parseColor("#999999");//xy轴画笔颜色
    protected int textColor = Color.parseColor("#555555");//xy轴画笔颜色
    protected float axisSize = Utils.sp2px(getContext(),10);//坐标轴的文字大小

    protected float padding = Utils.dp2px(getContext(),5);
    protected float marginXy = Utils.dp2px(getContext(),2);//离xy轴的距离
    protected float axisStrokeWidth = Utils.dp2px(getContext(),0.5f);//坐标轴线条宽度
    protected float lineStrokeWidth = Utils.dp2px(getContext(),1.5f);//折线宽度

    protected float dotX, dotY;//圆点xy
    protected int chartViewWidth;//view的宽度
    protected int chartViewHigth;//view的高度

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public BaseChartView(Context context) {
       this(context,null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public BaseChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public BaseChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /***
     * 初始化画笔
     */
    protected void initPaint(){
        //初始化坐标画笔
        xYAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xYAxisPaint.setColor(axisColor);
        xYAxisPaint.setAntiAlias(true);
        xYAxisPaint.setTextSize(axisSize);
        xYAxisPaint.setStrokeWidth(axisStrokeWidth);

        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setColor(axisColor);
        chartPaint.setAntiAlias(true);
        chartPaint.setStrokeWidth(lineStrokeWidth);
        chartPaint.setStyle(Paint.Style.FILL);
    }

}
