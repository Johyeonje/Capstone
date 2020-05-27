package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Float.parseFloat;

public class Chart1 extends AppCompatActivity {

    public String[] arr = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart1);

        Intent intent = getIntent();
        PieChart pieChart = findViewById(R.id.piechart1);
        String s = intent.getStringExtra("downString");

        /* ======================파이차트 부분==================== */
        tokenizer(s);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        /* 리스트에 값 삽입 */
        yValues.add(new PieEntry(parseFloat(arr[0]+"f"),"10대 남"));
        yValues.add(new PieEntry(parseFloat(arr[1]+"f"),"20대 남"));
        yValues.add(new PieEntry(parseFloat(arr[2]+"f"),"30대 남"));
        yValues.add(new PieEntry(parseFloat(arr[3]+"f"),"40대 남"));
        yValues.add(new PieEntry(parseFloat(arr[4]+"f"),"50대 이상 남"));
        yValues.add(new PieEntry(parseFloat(arr[5]+"f"),"10대 여"));
        yValues.add(new PieEntry(parseFloat(arr[6]+"f"),"20대 여"));
        yValues.add(new PieEntry(parseFloat(arr[7]+"f"),"30대 여"));
        yValues.add(new PieEntry(parseFloat(arr[8]+"f"),"40대 여"));
        yValues.add(new PieEntry(parseFloat(arr[9]+"f"),"50대 이상 여"));

        Description description = new Description();
        description.setText("연령대 차트"); //라벨
        description.setTextSize(20);
        pieChart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(yValues,"성/연령대");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }

    private void tokenizer(String s){ //토큰 분리 함수
        Integer i=0;
        String test = "10 8 6 4 2 10 8 6 4 2 ";
        StringTokenizer token1 = new StringTokenizer(test , " "); //인자1: 문자열, 인자2: 분리자
        while(token1.hasMoreTokens()) {
            arr[i] = token1.nextToken();
            System.out.println("arr" + i + " = " + arr[i]);
            i++;
        }
    }
}
