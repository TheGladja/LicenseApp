package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class DevicesActivity extends AppCompatActivity {
    private RecyclerView recViewDevices;
    private DeviceRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        initViews();

        adapter = new DeviceRecyclerViewAdapter(this);
        recViewDevices.setAdapter(adapter);
        recViewDevices.setLayoutManager(new LinearLayoutManager(this));
        adapter.setDevices(Utils.getInstance(this).getAllDevices());
    }

    private void initViews(){
        recViewDevices = findViewById(R.id.recViewDevices);
    }
}