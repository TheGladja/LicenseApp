package com.example.licenseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DeviceActivity extends AppCompatActivity {
    public static final String DEVICE_ID_KEY = "deviceId";
    private ImageView deviceImg;
    private TextView producerTxt, modelTxt, batteryCapacityTxt, chargingTimeTxt, longDescTxt;
    private ImageButton deviceItemNavBarMyDevice, deviceItemNavBarEnergyGraph;
    private Button deviceItemNavBarDeleteDevice;
    private Device incomingDevice;
    private boolean deviceExists = false;


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
                incomingDevice = Utils.getInstance(this).getdeviceById(deviceId, this);
                if(incomingDevice != null){
                    setData(incomingDevice);
                    deviceExists = true;
                }
            }
        }

        buttonViews();
    }

    private void buttonViews(){
        //Delete device from database
        //I have also implemented a dialog to confirm the deletion
        deviceItemNavBarDeleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this);
                builder.setTitle("Delete device");
                builder.setMessage("Are you sure you want to delete this device?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeviceDatabase deviceDatabase = new DeviceDatabase(DeviceActivity.this);
                        if(deviceExists){
                            boolean success = deviceDatabase.deleteDevice(incomingDevice);
                            if(success){
                                Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(DeviceActivity.this, "Device deleted successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DeviceActivity.this, "Error deleting device", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });

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
        chargingTimeTxt = findViewById(R.id.chargingTimeTxt);
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
        deviceChargingTime(device);
    }

    private void deviceChargingTime(Device device){
        // Retrieve the voltage from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        float voltage = prefs.getFloat("VOLTAGE", 0.0f);

        //Let's say if we have 5 volts we have 1000 mah (the maximum ammount of intensity(mAh))
        float intensity = voltage * 250;

        float chargingTime = (float )device.getBattery() / intensity;

        //Format the charging time to display only two decimal places
        String formattedChargingTime = String.format("%.2f", chargingTime);

        chargingTimeTxt.setText(formattedChargingTime + " hours");
    }
}