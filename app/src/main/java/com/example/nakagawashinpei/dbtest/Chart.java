package com.example.nakagawashinpei.dbtest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Chart extends AppCompatActivity {

    PieChart mPieChart;
    BarChart mBarChart;
    LineChart mLineChart;
    private Realm mRealm;
    private int sumGrade5 = 9;
    private int sumGrade4 = 5;
    private int sumGrade3 = 0;
    private int sumGrade2 = 0;
    private int sumGrade1 = 3;
    static final String PURPLE = "#8A2BE2";
    static final String BLOWN ="#8B4513";
    private enum ChartState {
        UNKNOUN,
        LineCahrt,
        BarChart
    };
    private ChartState mChartState = ChartState.LineCahrt;
    private RealmResults<Schedule> results;
    TextView mTextViewClimbCount;
    TextView mTextViewClimb;
    private Spinner spinner;
    String name;
    private Realm GymRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        //mLineChart = (LineChart) findViewById(R.id.line_chart) ;

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        results = mRealm.where(Schedule.class).findAll();

        initSetting();
        spinner = (Spinner) findViewById(R.id.spinner); //スピナ データ選択時の処理

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //選択されたアイテム名と位置（index)を内部変数へ保存
                name =  parent.getSelectedItem().toString();
                Log.d("", "onItemSelected:"+ name);
                LinearLayout layout = (LinearLayout)findViewById(R.id.layout_linechart);

                switch (name) {
                    case "級別の完登数":
                        // コンテンツ部分のLayoutを取ってくる
                        layout = (LinearLayout)findViewById(R.id.layout_nochart);
                        // 内容を全部消す
                        layout.removeAllViews();
                        // test_sub.xmlに変更する
                        getLayoutInflater().inflate(R.layout.linechart, layout);
                        setupLineChartView(results);
                        break;


                    case "日別の総完登数":
                        // コンテンツ部分のLayoutを取ってくる
                        layout = (LinearLayout)findViewById(R.id.layout_nochart);
                        // 内容を全部消す
                        layout.removeAllViews();
                        // test_sub.xmlに変更する
                        getLayoutInflater().inflate(R.layout.linechart, layout);
                        setDailyLineChart(results);
                        break;

                    case "ジム別のクライミング回数":
                        // コンテンツ部分のLayoutを取ってくる
                        layout = (LinearLayout)findViewById(R.id.layout_nochart);
                        // 内容を全部消す
                        layout.removeAllViews();
                        // test_sub.xmlに変更する
                        getLayoutInflater().inflate(R.layout.barchart, layout);
                        setupBarChartView();
                        break;

                    case "月別の完登数":
                        // コンテンツ部分のLayoutを取ってくる
                        layout = (LinearLayout)findViewById(R.id.layout_nochart);
                        // 内容を全部消す
                        layout.removeAllViews();
                        // test_sub.xmlに変更する
                        getLayoutInflater().inflate(R.layout.barchart, layout);
                        setupBarChartView();
                        break;
                }
            }
        });
    }


    private void setupPieChartView() {
        mPieChart.setUsePercentValues(true);
        //mPieChart.setDescription("チャートの説明");

        Legend legend = mPieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // 円グラフに表示するデータ
        //List<Integer> values = Arrays.asList(sumGrade5, sumGrade4, sumGrade3, sumGrade2, sumGrade1);
        List<Float> values = Arrays.asList(10f, 10f , 10f, 10f, 10f);
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entries.add(new PieEntry(values.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "チャートのラベル");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        List<String> labels = Arrays.asList("A", "B", "C", "D");
        //PieData pieData = new PieData(labels, dataSet);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.WHITE);
        mPieChart.animateXY(2000, 2000); // 表示アニメーション
        mPieChart.setData(pieData);
    }

    private void setupBarChartView() {
        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        RealmResults<Schedule> results1;
        mBarChart.getAxisLeft().setDrawZeroLine(true);
        initSetting();

        List<BarEntry> entries = new ArrayList<>();

        //X軸


        //Y軸(左側)
        YAxis yAxisleft = mBarChart.getAxisLeft();
        yAxisleft.setAxisMinimum(0f);

        //Y軸(右側)
        YAxis yAxisRight = mBarChart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setDrawGridLines(false);

        /*ジム別のクライミング回数の取得*/
        GymRealm = Realm.getDefaultInstance();
        RealmResults<Gym> Gymresults;
        Gymresults = GymRealm.where(Gym.class).findAll();
        final String[] labels = new String[Gymresults.size()];
        //labels[0] = "";
        //for(int i = 0; i < Gymresults.size(); i++){
        for(int i = 0; i < 20; i++){
            //results1 = mRealm.where(Schedule.class).equalTo("title",Gymresults.get(i).getGym()).findAll();
            entries.add(new BarEntry(i,i));
            //labels[i] = Gymresults.get(i).getGym();
        }

        XAxis xAxis = mBarChart.getXAxis();
        XAxis bottomAxis = mBarChart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        //データに名前をつける
        BarDataSet dataSet = new BarDataSet(entries, "級別の上った数");
        //整数で表示
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setValueTextColor(Color.BLACK);
        barData.setValueTextSize(20f);
        mBarChart.animateXY(2000,2000);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setData(barData);;


    }

    public void setDailyLineChart(RealmResults<Schedule> results){
        mLineChart = (LineChart) findViewById(R.id.line_chart) ;
        initSetting();

        //X軸
        XAxis xAxis = mLineChart.getXAxis();
        XAxis bottomAxis = mLineChart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        //Y軸(左側)
        YAxis yAxisleft = mLineChart.getAxisLeft();
        yAxisleft.setAxisMinimum(0f);

        //Y軸(右側)
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setDrawGridLines(false);

        List<Entry> entriesGrade5 = new ArrayList<>();
        List<Entry> entriesGrade4 = new ArrayList<>();
        List<Entry> entriesGrade3 = new ArrayList<>();
        List<Entry> entriesGrade2 = new ArrayList<>();
        List<Entry> entriesGrade1 = new ArrayList<>();

        entriesGrade5.add(new Entry(0,0));
        entriesGrade4.add(new Entry(0,0));
        entriesGrade3.add(new Entry(0,0));
        entriesGrade2.add(new Entry(0,0));
        entriesGrade1.add(new Entry(0,0));

        //各Gradeのデータ登録
        for(int i = 0; i < results.size(); i++){
            entriesGrade5.add(new Entry(i+1,results.get(i).getGrade5()));
            entriesGrade4.add(new Entry(i+1,results.get(i).getGrade4()));
            entriesGrade3.add(new Entry(i+1,results.get(i).getGrade3()));
            entriesGrade2.add(new Entry(i+1,results.get(i).getGrade2()));
            entriesGrade1.add(new Entry(i+1,results.get(i).getGrade1()));
        }

        //各Gradeのグラフの設定
        LineDataSet dataSetGrade5 = setLineDataOfGrade(entriesGrade5,Color.RED,"5級");
        LineDataSet dataSetGrade4 = setLineDataOfGrade(entriesGrade4,Color.parseColor(PURPLE),"4級");
        LineDataSet dataSetGrade3 = setLineDataOfGrade(entriesGrade3,Color.GREEN,"3級");
        LineDataSet dataSetGrade2 = setLineDataOfGrade(entriesGrade2,Color.parseColor(BLOWN),"2級");
        LineDataSet dataSetGrade1 = setLineDataOfGrade(entriesGrade1,Color.BLACK,"1級");



        LineData lineData = new LineData(dataSetGrade5,dataSetGrade4,dataSetGrade3,dataSetGrade2,dataSetGrade1);
        lineData.setValueTextColor(Color.BLACK);
        lineData.setValueTextSize(7f);
        lineData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        mLineChart.animateXY(1000,2000);
        mLineChart.setData(lineData);

    }

    public void setupLineChartView(RealmResults<Schedule> results){
        mLineChart = (LineChart) findViewById(R.id.line_chart) ;
        initSetting();
        //mLineChart = null;


        int sumGrade5 = 0;
        int sumGrade4 = 0;
        int sumGrade3 = 0;
        int sumGrade2 = 0;
        int sumGrade1 = 0;

        //X軸
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setLabelRotationAngle(5);


        //X軸に表示するLabelのリスト(最初の""は原点の位置)
        //final String[] labels = {"08/18", "08/19", "08/20","08/20","08/20","08/18", "08/19", "08/20","08/20","08/20"};
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(labels));


        XAxis bottomAxis = mLineChart.getXAxis();
        xAxis.setAxisLineWidth(1f);
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(true);
        bottomAxis.setDrawAxisLine(false);



        //Y軸(左側)
        YAxis yAxisleft = mLineChart.getAxisLeft();
        yAxisleft.setAxisMinimum(0f);

        //Y軸(右側)
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setDrawGridLines(false);




        List<Entry> entriesGrade5 = new ArrayList<>();
        List<Entry> entriesGrade4 = new ArrayList<>();
        List<Entry> entriesGrade3 = new ArrayList<>();
        List<Entry> entriesGrade2 = new ArrayList<>();
        List<Entry> entriesGrade1 = new ArrayList<>();

        entriesGrade5.add(new Entry(0,0));
        entriesGrade4.add(new Entry(0,0));
        entriesGrade3.add(new Entry(0,0));
        entriesGrade2.add(new Entry(0,0));
        entriesGrade1.add(new Entry(0,0));

        final String[] label = new String[results.size()+1];
        label[0] = "";
        //各Gradeのデータ登録
        for(int i = 0; i < results.size(); i++){
            sumGrade5 = sumGrade5 + results.get(i).getGrade5();
            sumGrade4 = sumGrade4 + results.get(i).getGrade4();
            sumGrade3 = sumGrade3 + results.get(i).getGrade3();
            sumGrade2 = sumGrade2 + results.get(i).getGrade2();
            sumGrade1 = sumGrade1 + results.get(i).getGrade1();

            entriesGrade5.add(new Entry(i+1,sumGrade5));
            entriesGrade4.add(new Entry(i+1,sumGrade4));
            entriesGrade3.add(new Entry(i+1,sumGrade3));
            entriesGrade2.add(new Entry(i+1,sumGrade2));
            entriesGrade1.add(new Entry(i+1,sumGrade1));

            /*ラベルの設定*/
            Date date = results.get(i).getData();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
            label[i+1] = sdf.format(date).toString();
        }
        xAxis.setValueFormatter(new MyXAxisValueFormatter(label));

        //各Gradeのグラフの設定
        LineDataSet dataSetGrade5 = setLineDataOfGrade(entriesGrade5,Color.RED,"5級");
        LineDataSet dataSetGrade4 = setLineDataOfGrade(entriesGrade4,Color.parseColor(PURPLE),"4級");
        LineDataSet dataSetGrade3 = setLineDataOfGrade(entriesGrade3,Color.GREEN,"3級");
        LineDataSet dataSetGrade2 = setLineDataOfGrade(entriesGrade2,Color.parseColor(BLOWN),"2級");
        LineDataSet dataSetGrade1 = setLineDataOfGrade(entriesGrade1,Color.BLACK,"1級");

        LineData lineData = new LineData(dataSetGrade5,dataSetGrade4,dataSetGrade3,dataSetGrade2,dataSetGrade1);
        lineData.setValueTextColor(Color.BLACK);
        lineData.setValueTextSize(7f);
        lineData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });

        mLineChart.animateXY(1000,2000);
        mLineChart.setData(lineData);
    }

    public LineDataSet setLineDataOfGrade(List<Entry> entries, int color, String grade){
        LineDataSet dataSet = new LineDataSet(entries, grade);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setDrawCircles(true);
        return dataSet;
    }

    private void initSetting(){
        mTextViewClimbCount = findViewById(R.id.TotalClimbingCount);
        mTextViewClimb = findViewById(R.id.TotalCliming);

        int TotalClimbingCount =
                results.sum("grade5").intValue()
                        + results.sum("grade4").intValue()
                        + results.sum("grade3").intValue()
                        + results.sum("grade2").intValue()
                        + results.sum("grade1").intValue();
        mTextViewClimbCount.setText("総完登課題数：" + TotalClimbingCount);
        mTextViewClimb.setText("総クライミング日数：" + results.size());
    }

    public void toast(String string){
        Toast.makeText(this, string,Toast.LENGTH_SHORT).show();
    }

    public void onMemoryButtonTapped(View view){
        startActivity(new Intent(Chart.this,MainActivity.class));
        finish();
    }

    public void onChartButtonTapped(View view){
        startActivity(new Intent(Chart.this,Chart.class));
        finish();
    }

    public void onAddGymButtonTapped(View view){
        startActivity(new Intent(Chart.this,AddGymActivity.class));
        finish();
    }
}

