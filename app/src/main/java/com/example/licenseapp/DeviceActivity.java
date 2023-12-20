package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DeviceActivity extends AppCompatActivity {
    public static final String DEVICE_ID_KEY = "deviceId";
    private ImageView deviceImg;
    private TextView producerTxt, modelTxt, batteryCapacityTxt, longDescTxt;
    private ImageButton deviceItemNavBarMyDevice, deviceItemNavBarEnergyGraph;
    private Button deviceItemNavBarDeleteDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        initViews();

        //Get the incoming data from the recycler view
        Intent intent = getIntent();
        if(intent != null){
            int deviceId = intent.getIntExtra(DEVICE_ID_KEY, -1);
            if(deviceId != -1){
                Device incomingDevice = Utils.getInstance(this).getdeviceById(deviceId, this);
                if(incomingDevice != null){
                    setData(incomingDevice);
                }
            }
        }

        buttonViews();
    }

    //TODO: make the delete button work
    private void buttonViews(){
        deviceItemNavBarMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, MyDeviceActivity.class);
                startActivity(intent);
            }
        });

        deviceItemNavBarEnergyGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, EnergyGraphActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        deviceImg = findViewById(R.id.deviceImage);
        producerTxt = findViewById(R.id.producerTxt);
        modelTxt = findViewById(R.id.modelTxt);
        batteryCapacityTxt = findViewById(R.id.batteryCapacityTxt);
        longDescTxt = findViewById(R.id.longDescTxt);
        deviceItemNavBarMyDevice = findViewById(R.id.deviceItemNavBarMyDevice);
        deviceItemNavBarEnergyGraph = findViewById(R.id.deviceItemNavBarEnergyGraph);
        deviceItemNavBarDeleteDevice = findViewById(R.id.deviceItemNavBarDeleteDevice);
    }

    private void setData(Device device){
        Glide.with(this).asBitmap().load(device.getImgUrl()).into(deviceImg);
        producerTxt.setText(device.getProducer());
        modelTxt.setText(device.getModel());
        batteryCapacityTxt.setText(String.valueOf(device.getBattery()) + " mAh");
        longDescTxt.setText(device.getLongDesc());
    }
}