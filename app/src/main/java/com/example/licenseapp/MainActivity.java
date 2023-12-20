package com.example.licenseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnAbout, btnDevices, btnEnergyGraph, btnBestChargingDevice, btnMyDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        buttonMyDevice();
        buttonDevices();
        buttonBestChargingDevice();
        buttonEnergyGraph();
        buttonAboutAction();
    }

    private void initViews(){
        btnMyDevice = findViewById(R.id.btnMyDevice);
        btnDevices = findViewById(R.id.btnDevices);
        btnBestChargingDevice = findViewById(R.id.btnBestChargingDevice);
        btnEnergyGraph = findViewById(R.id.btnEnergyGraph);
        btnAbout = findViewById(R.id.btnAbout);
    }

    private void buttonMyDevice(){
        btnMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonDevices(){
        btnDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DevicesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonBestChargingDevice(){
        btnBestChargingDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BestChargingDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonEnergyGraph(){
        btnEnergyGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnergyGraphActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonAboutAction(){
        //Create an dialog that sends the user to my website link
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage("Designed and Developed by Calin at https://github.com/TheGladja?tab=repositories\n" +
                        "Check my website for more projects:");
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
                        //Parse the link of my github page so the user can see my other projects
                        intent.putExtra("url", "https://github.com/TheGladja?tab=repositories");
                        startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });
    }
}