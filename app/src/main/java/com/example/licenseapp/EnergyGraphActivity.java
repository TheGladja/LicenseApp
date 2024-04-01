package com.example.licenseapp;

import android.content.SharedPreferences;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnergyGraphActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private List<BarEntry> entriesBarChart = new ArrayList<>();
    private List<String> labelsBarChart = new ArrayList<>();
    private List<PieEntry> entriesPieChart = new ArrayList<>();
    private boolean appFirstOpened = false;


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
            appFirstOpened = true;
        }

        if (!appFirstOpened) {
            //Add the biggest energy value that was read in that moment
            addCurrentEnergySet();
        }

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
        xAxis.setLabelRotationAngle(90);
        xAxis.setLabelCount(labelsBarChart.size()); // Set the number of labels to display

        // Apply the custom ValueFormatter to the y-axis of the bar chart
        barChart.getAxisLeft().setValueFormatter(valueFormatter);

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

        // Apply the custom ValueFormatter to the data set of the pie chart
        pieDataSet.setValueFormatter(valueFormatter);

        // Disable description label
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        // Refresh the pie chart
        pieChart.invalidate();
    }

    // Create a custom ValueFormatter to format the y-axis values with two decimal points
    ValueFormatter valueFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            // Format the value to have two decimal points
            return String.format(Locale.getDefault(), "%.2f", value);
        }
    };

    private void addCurrentEnergySet() {
        GraphDatabase graphDatabase = new GraphDatabase(this);
        List<GraphModel> graphSetsList = graphDatabase.getAllGraphSets();
        // Get the current date
        LocalDate localDate = LocalDate.now();
        // Define a format for the date string (optional)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // Convert the date to a string using the defined format
        String date = localDate.format(dateTimeFormatter);

        // Retrieve the voltage from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        float voltage = prefs.getFloat("VOLTAGE", 0.0f);
        boolean newReadVoltage = prefs.getBoolean("NEW_READ_VOLTAGE", false);

        GraphModel newGraphModel = new GraphModel(1, date, voltage);

        // If a new voltage was read using the bluetooth connection
        if (newReadVoltage) {
            //The voltage will be seen as read so we need to update the shared preferences
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.putBoolean("NEW_READ_VOLTAGE", false);
            editor.apply();
            //If the newest read energy is the greatest in that day we update the energy graph
            //Else if this is the first time we read the energy in that day we just add it to the database
            GraphModel lastestEnergyRead = graphSetsList.get(graphSetsList.size() - 1);
            if (lastestEnergyRead.getDate().equals(newGraphModel.getDate())) {
                if (newGraphModel.getEnergy() > lastestEnergyRead.getEnergy()) {
                    try {
                        graphDatabase.deleteGraphSet(lastestEnergyRead);
                        graphDatabase.addGraphSet(newGraphModel);
                        Toast.makeText(this, "Bigger energy value added to the graphs", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error adding a bigger energy value", Toast.LENGTH_SHORT).show();
                    }

                    //Update the graphs
                    entriesBarChart.clear();
                    labelsBarChart.clear();
                    entriesPieChart.clear();
                    getDataSets();
                }
            } else {
                try {
                    graphDatabase.addGraphSet(newGraphModel);
                    Toast.makeText(this, "New energy value added to the graphs", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error adding new energy value", Toast.LENGTH_SHORT).show();
                }

                //Update the graphs
                entriesBarChart.clear();
                labelsBarChart.clear();
                entriesPieChart.clear();
                getDataSets();
            }
        }
    }

    private void getDataSets() {
        GraphDatabase graphDatabase = new GraphDatabase(this);
        List<GraphModel> graphSetsList = graphDatabase.getAllGraphSets();
        int count = 0;

        //For deleting all the chart values
        //graphDatabase.deleteAllGraphSets();

        for (GraphModel gp : graphSetsList) {
            entriesBarChart.add(new BarEntry(count++, gp.getEnergy()));
            labelsBarChart.add(gp.getDate());
            entriesPieChart.add(new PieEntry(gp.getEnergy(), 0));
        }
    }

    private void initDataSets() {
        GraphDatabase graphDatabase = new GraphDatabase(this);
        // Get the current date
        LocalDate localDate = LocalDate.now();
        // Define a format for the date string (optional)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // Convert the date to a string using the defined format
        String date = localDate.format(dateTimeFormatter);

        //Initial voltage value is set to 0
        GraphModel newGraphModel = new GraphModel(1, date, 0.0f);

        try {
            graphDatabase.addGraphSet(newGraphModel);
            Toast.makeText(this, "Graph initialized successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing graph", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
    }
}

