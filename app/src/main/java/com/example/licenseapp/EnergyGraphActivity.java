package com.example.licenseapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnergyGraphActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private List<BarEntry> entriesBarChart = new ArrayList<>();
    private List<String> labelsBarChart = new ArrayList<>();
    private List<PieEntry> entriesPieChart = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy_graph);

        initViews();

        //Values for the graph
        getDataSets();

        //Initialize values for the graph if the app is first started and get the values
        if (entriesBarChart.size() == 0) {
            initDataSets();
            getDataSets();
        }

        //Add the biggest energy value that was read in that moment
        addCurrentEnergySet();

        //FOR THE BAR CHART
        displayBarChart();

        //FOR THE PIE CHART
        displayPieChart();
    }

    private void displayBarChart() {
        BarDataSet barDataSet = new BarDataSet(entriesBarChart, "");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Color bar data set
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        // Text color
        barDataSet.setValueTextColor(Color.BLACK);
        // Text size
        barDataSet.setValueTextSize(16f);

        // Customize the x-axis to be a string so it would display the date when the energy value was read
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsBarChart));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelRotationAngle(45);
        xAxis.setLabelCount(labelsBarChart.size()); // Set the number of labels to display

        // Disable description label
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        // Refresh the bar chart
        barChart.invalidate();
    }

    private void displayPieChart() {
        PieDataSet pieDataSet = new PieDataSet(entriesPieChart, "");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        //Select the color for the inner circle
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.parseColor("#FFFDD0"));

        // Color pie data set
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        // Text color
        pieDataSet.setValueTextColor(Color.BLACK);
        // Text size
        pieDataSet.setValueTextSize(16f);

        // Disable description label
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        // Refresh the pie chart
        pieChart.invalidate();
    }

    //TODO: connection with the bluetooth
    private void addCurrentEnergySet() {
        //*******
        Random random = new Random();
        int randomNumber = random.nextInt(91) + 10; // Generates a random number between 0 and 90, then adds 10
        //*******
        GraphDatabase graphDatabase = new GraphDatabase(this);
        List<GraphModel> graphSetsList = graphDatabase.getAllGraphSets();
        // Get the current date
        LocalDate localDate = LocalDate.now();
        // Define a format for the date string (optional)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // Convert the date to a string using the defined format
        String date = localDate.format(dateTimeFormatter);

        GraphModel newGraphModel = new GraphModel(1, date,  randomNumber);

        //If the newest read energy is the greatest in that day we update the energy graph
        //Else if this is the first time we read the energy in that day we just add it to the database
        GraphModel lastestEnergyRead = graphSetsList.get(graphSetsList.size() - 1);
        if(lastestEnergyRead.getDate().equals(newGraphModel.getDate())){
            if(newGraphModel.getEnergy() > lastestEnergyRead.getEnergy()){
                graphDatabase.deleteGraphSet(lastestEnergyRead);
                graphDatabase.addGraphSet(newGraphModel);
                Toast.makeText(this, "Bigger energy value added to the graphs", Toast.LENGTH_SHORT).show();

                //Update the graphs
                entriesBarChart.clear();
                labelsBarChart.clear();
                entriesPieChart.clear();
                getDataSets();
            }
        }else{
            graphDatabase.addGraphSet(newGraphModel);
            Toast.makeText(this, "New energy value added to the graphs", Toast.LENGTH_SHORT).show();

            //Update the graphs
            entriesBarChart.clear();
            labelsBarChart.clear();
            entriesPieChart.clear();
            getDataSets();
        }
    }

    private void getDataSets() {
        GraphDatabase graphDatabase = new GraphDatabase(this);
        List<GraphModel> graphSetsList = graphDatabase.getAllGraphSets();
        int count = 0;

        for (GraphModel gp : graphSetsList) {
            entriesBarChart.add(new BarEntry(count++, gp.getEnergy()));
            labelsBarChart.add(gp.getDate());
            entriesPieChart.add(new PieEntry(gp.getEnergy(), 0));
        }
    }

    private void initDataSets() {
        GraphDatabase graphDatabase = new GraphDatabase(this);
        List<GraphModel> graphModelList = new ArrayList<>();
        boolean success = true;

        try {
            graphModelList.add(new GraphModel(1, "30.04.2013", 68));
            graphModelList.add(new GraphModel(2, "03.05.2013", 13));
            graphModelList.add(new GraphModel(3, "22.05.2013", 31));
            graphModelList.add(new GraphModel(4, "27.05.2013", 22));
            graphModelList.add(new GraphModel(5, "18.06.2013", 100));
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing graph sets", Toast.LENGTH_SHORT).show();
        }

        for (GraphModel gp : graphModelList) {
            try {
                graphDatabase.addGraphSet(gp);
            } catch (Exception e) {
                success = false;
            }
        }
        if (success) {
            Toast.makeText(this, "Graph sets initialized successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error initializing graph sets", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
    }
}

