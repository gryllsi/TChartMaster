package com.totcy.chartmaster;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.totcy.tchartlibrary.charts.BarChartView;
import com.totcy.tchartlibrary.charts.LineChartView;
import com.totcy.tchartlibrary.entity.Entry;
import com.totcy.tchartlibrary.entity.LineData;
import com.totcy.tchartlibrary.entity.LineDataSet;

import java.util.ArrayList;

import static com.totcy.chartmaster.R.id.barChartView;

/**
 * Description 图表库之折线图示例
 * Author: tu
 * Date: 2016-11-24
 * Time: 11:15
 */
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private LineChartView mLineChartView;
    private Switch mSwitch1, mSwitch2, mSwitch3;

    private LineData mLineData;
    private BarChartView barChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        //barChartView.setLineData(mLineData);
    }

    private void initView() {
        mLineChartView = (LineChartView) findViewById(R.id.lineChart);
        barChartView = (BarChartView) findViewById(R.id.barChartView);

        mSwitch1 = (Switch) findViewById(R.id.switch1);
        mSwitch2 = (Switch) findViewById(R.id.switch2);
        mSwitch3 = (Switch) findViewById(R.id.switch3);
        mSwitch1.setOnCheckedChangeListener(this);
        mSwitch2.setOnCheckedChangeListener(this);
        mSwitch3.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化view
     */
    private void initData() {
        //just for test
        //X轴标签
        ArrayList<String> lables = new ArrayList<>();
        lables.add("一月");
        lables.add("二月");
        lables.add("三月");
        lables.add("四月");
        lables.add("五月");
        lables.add("六月");

        //折线集合
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        //折线数据1
        LineDataSet lineDataSet1 = new LineDataSet();
        lineDataSet1.setDotColor(Color.RED);
        lineDataSet1.setLineColor(Color.parseColor("#0696f5"));

        //折线数据1 Y value
        ArrayList<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(120, 0));
        entries1.add(new Entry(20, 1));
        entries1.add(new Entry(80, 2));
        entries1.add(new Entry(37, 3));
        entries1.add(new Entry(94, 4));
        entries1.add(new Entry(234, 5));
        lineDataSet1.setyVals(entries1);
        lineDataSets.add(lineDataSet1);

        //折线数据2
        LineDataSet lineDataSet2 = new LineDataSet();
        lineDataSet2.setDotColor(Color.RED);
        lineDataSet2.setLineColor(Color.parseColor("#60b027"));

        //折线数据2 Y value
        ArrayList<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(50, 0));
        entries2.add(new Entry(70, 1));
        entries2.add(new Entry(150, 2));
        entries2.add(new Entry(77, 3));
        entries2.add(new Entry(467, 4));
        entries2.add(new Entry(124, 5));
        lineDataSet2.setyVals(entries2);
        lineDataSets.add(lineDataSet2);

        //折线数据3
        LineDataSet lineDataSet3 = new LineDataSet();
        lineDataSet3.setDotColor(Color.RED);
        lineDataSet3.setLineColor(Color.parseColor("#f82522"));

        //折线数据3 Y value
        ArrayList<Entry> entries3 = new ArrayList<>();
        entries3.add(new Entry(420, 0));
        entries3.add(new Entry(80, 1));
        entries3.add(new Entry(120, 2));
        entries3.add(new Entry(197, 3));
        entries3.add(new Entry(307, 4));
        entries3.add(new Entry(184, 5));
        lineDataSet3.setyVals(entries3);
        lineDataSets.add(lineDataSet3);


        mLineData = new LineData(lables, lineDataSets);
    }

    public void onClick(View view) {
        barChartView.setLineData(mLineData);
        mLineChartView.setLineDataWithAnim(mLineData);
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switch1:
                mLineChartView.setSmooth(isChecked);
                break;
            case R.id.switch2:
                mLineChartView.setIntersection(isChecked);
                break;
            case R.id.switch3:
                mLineChartView.setShowValue(isChecked);
                break;

        }
    }
}
