package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DevicesActivity extends AppCompatActivity {
    private RecyclerView recViewDevices;
    private DeviceRecyclerViewAdapter adapter;
    private ImageButton devicesListNavBarMyDevice, devicesListNavBarEnergyGraph;
    private Button devicesListNavBarAddDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        initViews();

        adapter = new DeviceRecyclerViewAdapter(this);
        recViewDevices.setAdapter(adapter);
        recViewDevices.setLayoutManager(new LinearLayoutManager(this));
        adapter.setDevices(Utils.getInstance(this).getAllDevices(this));

        buttonViews();
    }

    //TODO: make the add device button work
    private void buttonViews(){
        devicesListNavBarMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicesActivity.this, MyDeviceActivity.class);
                startActivity(intent);
            }
        });

        devicesListNavBarEnergyGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicesActivity.this, EnergyGraphActivity.class);
                startActivity(intent);
            }
        });

        devicesListNavBarAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicesActivity.this, AddDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        recViewDevices = findViewById(R.id.recViewDevices);
        devicesListNavBarMyDevice = findViewById(R.id.devicesListNavBarMyDevice);
        devicesListNavBarEnergyGraph = findViewById(R.id.devicesListNavBarEnergyGraph);
        devicesListNavBarAddDevice = findViewById(R.id.devicesListNavBarAddDevice);
    }
}