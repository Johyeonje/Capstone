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

public class Chart2 extends AppCompatActivity {

    public String[] arr_cal = new String[10];
    public float[] result = new float[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);

        Intent intent = getIntent();
        PieChart pieChart = findViewById(R.id.piechart2);
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

        //게임 카테고리 계산
        result[0] = parseFloat(arr_cal[0])*67.9f +
                parseFloat(arr_cal[1])*51.0f + parseFloat(arr_cal[2])*32.4f +
                parseFloat(arr_cal[3])*24.4f + parseFloat(arr_cal[4])*8.4f +
                parseFloat(arr_cal[5])*44.0f + parseFloat(arr_cal[6])*20.0f +
                parseFloat(arr_cal[7])*9.5f + parseFloat(arr_cal[8])*4.1f +
                parseFloat(arr_cal[9])*3.4f;

        //가전/컴퓨터 카테고리 계산
        result[1] = parseFloat(arr_cal[0])*30.4f +
                parseFloat(arr_cal[1])*17.3f + parseFloat(arr_cal[2])*13.9f +
                parseFloat(arr_cal[3])*20.3f + parseFloat(arr_cal[4])*19.3f +
                parseFloat(arr_cal[5])*0.0f + parseFloat(arr_cal[6])*8.4f +
                parseFloat(arr_cal[7])*5.7f + parseFloat(arr_cal[8])*3.3f +
                parseFloat(arr_cal[9])*7.6f;

        //휴대폰/태블릿 카테고리 계산
        result[2] = parseFloat(arr_cal[0])*30.4f +
                parseFloat(arr_cal[1])*30.8f + parseFloat(arr_cal[2])*17.6f +
                parseFloat(arr_cal[3])*13.8f + parseFloat(arr_cal[4])*8.4f +
                parseFloat(arr_cal[5])*8.0f + parseFloat(arr_cal[6])*6.3f +
                parseFloat(arr_cal[7])*6.7f + parseFloat(arr_cal[8])*5.0f +
                parseFloat(arr_cal[9])*5.0f;

        //여행/숙박/항공 카테고리 계산
        result[3] = parseFloat(arr_cal[0])*10.7f +
                parseFloat(arr_cal[1])*17.3f + parseFloat(arr_cal[2])*34.3f +
                parseFloat(arr_cal[3])*26.0f + parseFloat(arr_cal[4])*35.3f +
                parseFloat(arr_cal[5])*10.0f + parseFloat(arr_cal[6])*26.3f +
                parseFloat(arr_cal[7])*21.0f + parseFloat(arr_cal[8])*24.8f +
                parseFloat(arr_cal[9])*28.6f;

        //자동차 카테고리 계산
        result[4] = parseFloat(arr_cal[0])*10.7f +
                parseFloat(arr_cal[1])*12.5f + parseFloat(arr_cal[2])*19.4f +
                parseFloat(arr_cal[3])*30.1f + parseFloat(arr_cal[4])*20.2f +
                parseFloat(arr_cal[5])*0.0f + parseFloat(arr_cal[6])*2.1f +
                parseFloat(arr_cal[7])*2.9f + parseFloat(arr_cal[8])*2.5f +
                parseFloat(arr_cal[9])*1.7f;

        //음악/공연/영화 카테고리 계산
        result[5] = parseFloat(arr_cal[0])*48.2f +
                parseFloat(arr_cal[1])*52.9f + parseFloat(arr_cal[2])*57.4f +
                parseFloat(arr_cal[3])*64.2f + parseFloat(arr_cal[4])*58.8f +
                parseFloat(arr_cal[5])*64.0f + parseFloat(arr_cal[6])*60.0f +
                parseFloat(arr_cal[7])*58.1f + parseFloat(arr_cal[8])*60.3f +
                parseFloat(arr_cal[9])*47.1f;

        //뷰티/화장품 카테고리 계산
        result[6] = parseFloat(arr_cal[0])*0.0f +
                parseFloat(arr_cal[1])*10.6f + parseFloat(arr_cal[2])*7.4f +
                parseFloat(arr_cal[3])*4.9f + parseFloat(arr_cal[4])*0.8f +
                parseFloat(arr_cal[5])*54.0f + parseFloat(arr_cal[6])*62.1f +
                parseFloat(arr_cal[7])*51.4f + parseFloat(arr_cal[8])*36.4f +
                parseFloat(arr_cal[9])*27.7f;

        //패션/잡화 카테고리 계산
        result[7] = parseFloat(arr_cal[0])*8.9f +
                parseFloat(arr_cal[1])*10.6f + parseFloat(arr_cal[2])*16.7f +
                parseFloat(arr_cal[3])*9.8f + parseFloat(arr_cal[4])*5.0f +
                parseFloat(arr_cal[5])*22.0f + parseFloat(arr_cal[6])*23.2f +
                parseFloat(arr_cal[7])*10.5f + parseFloat(arr_cal[8])*25.6f +
                parseFloat(arr_cal[9])*34.5f;

        //식/음료 카테고리 계산
        result[8] = parseFloat(arr_cal[0])*8.9f +
                parseFloat(arr_cal[1])*11.5f + parseFloat(arr_cal[2])*6.5f +
                parseFloat(arr_cal[3])*13.0f + parseFloat(arr_cal[4])*10.9f +
                parseFloat(arr_cal[5])*18.0f + parseFloat(arr_cal[6])*24.2f +
                parseFloat(arr_cal[7])*16.2f + parseFloat(arr_cal[8])*16.5f +
                parseFloat(arr_cal[9])*26.9f;

        //쇼핑몰/유통점 카테고리 계산
        result[9] = parseFloat(arr_cal[0])*5.4f +
                parseFloat(arr_cal[1])*6.7f + parseFloat(arr_cal[2])*7.4f +
                parseFloat(arr_cal[3])*10.6f + parseFloat(arr_cal[4])*17.6f +
                parseFloat(arr_cal[5])*10.0f + parseFloat(arr_cal[6])*6.3f +
                parseFloat(arr_cal[7])*6.7f + parseFloat(arr_cal[8])*19.0f +
                parseFloat(arr_cal[9])*20.2f;

        /* 리스트에 값 삽입 */
        yValues.add(new PieEntry(result[0],"게임"));
        yValues.add(new PieEntry(result[1],"가전/컴퓨터"));
        yValues.add(new PieEntry(result[2],"휴대폰/태블릿"));
        yValues.add(new PieEntry(result[3],"여행/숙박/항공"));
        yValues.add(new PieEntry(result[4],"자동차"));
        yValues.add(new PieEntry(result[5],"음악/공연/영화"));
        yValues.add(new PieEntry(result[6],"뷰티/화장품"));
        yValues.add(new PieEntry(result[7],"패션/잡화"));
        yValues.add(new PieEntry(result[8],"식/음료"));
        yValues.add(new PieEntry(result[9],"쇼핑몰/유통점"));

        Description description = new Description();
        description.setText("선호도 차트"); //라벨
        description.setTextSize(20);
        pieChart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(yValues,"카테고리");
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
            arr_cal[i] = token1.nextToken(); //기존 값 넣기
            i++;
        }
    }
}
