package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class DeveloperData extends AppCompatActivity {


    // erstezen durch mqtt oder array aus anderer activit
       int [] data={20,10,0,3,7};



    LineChart mpLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_data);


        // create chart
        mpLineChart = (LineChart) findViewById(R.id.linechart);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Mqtt data");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();





        // Chart styling
        mpLineChart.setDrawGridBackground(true);
        mpLineChart.setDrawBorders(true);
        //mpLineChart.setBorderColor();

        Description description = new Description();
        description.setText("Diagramm");
        //description.setTextColor();
        description.setTextSize(20);
        mpLineChart.setDescription(description);

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(true);






    }




       private ArrayList<Entry> dataValues1()
       {
           ArrayList<Entry> dataVals = new ArrayList<Entry>();
           //ArrayList<String> mqttDataList = mqttManager.getMqttDataList();
           for (int i = 0; i < data.length; i++) {


               dataVals.add(new Entry(i,data[i]));


           }


           return dataVals;


       }










}