package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BestChargingDeviceActivity extends AppCompatActivity {
    private ImageView deviceImg;
    private TextView producerTxt, modelTxt, batteryCapacityTxt, longDescTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_charging_device);

        initViews();

        //Display the best charging device
        int min = 100000;
        Device bestDevice = null;
        List<Device> devices = Utils.getInstance(this).getAllDevices(this);
        for(Device d : devices){
            if(d.getBattery() < min){
                min = d.getBattery();
                bestDevice = d;
            }
        }
        if(bestDevice != null){
            setData(bestDevice);
        }
    }

    private void initViews(){
        deviceImg = findViewById(R.id.deviceImage);
        producerTxt = findViewById(R.id.producerTxt);
        modelTxt = findViewById(R.id.modelTxt);
        batteryCapacityTxt = findViewById(R.id.batteryCapacityTxt);
        longDescTxt = findViewById(R.id.longDescTxt);
    }

    private void setData(Device device){
        Glide.with(this).asBitmap().load(device.getImgUrl()).into(deviceImg);
        System.out.println(device.getImgUrl());
        producerTxt.setText(device.getProducer());
        modelTxt.setText(device.getModel());
        batteryCapacityTxt.setText(String.valueOf(device.getBattery()) + " mAh");
        longDescTxt.setText(device.getLongDesc());
    }
}