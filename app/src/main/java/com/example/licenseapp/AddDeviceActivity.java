package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddDeviceActivity extends AppCompatActivity {
    private EditText editTxtProducer, editTxtModel, editTxtBatteryCapacity, editTxtShortDesc, editTxtLongDesc;
    private Button btnCreateDevice;
    private ProgressBar progressBarAddDevice;
    private TextView txtWarningProducer, txtWarningModel, txtWarningBatteryCapacity, txtWarningShortDesc, txtWarningLongDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        initViews();
        buttonAddDevice();
    }

    private void initViews(){
        editTxtProducer = findViewById(R.id.editTxtProducer);
        editTxtModel = findViewById(R.id.editTxtModel);
        editTxtBatteryCapacity = findViewById(R.id.editTxtBatteryCapacity);
        editTxtShortDesc = findViewById(R.id.editTxtShortDesc);
        editTxtLongDesc = findViewById(R.id.editTxtLongDesc);
        btnCreateDevice = findViewById(R.id.btnCreateDevice);
        progressBarAddDevice = findViewById(R.id.progressBarAddDevice);
        txtWarningProducer = findViewById(R.id.txtWarningProducer);
        txtWarningModel = findViewById(R.id.txtWarningModel);
        txtWarningBatteryCapacity = findViewById(R.id.txtWarningBatteryCapacity);
        txtWarningShortDesc = findViewById(R.id.txtWarningShortDesc);
        txtWarningLongDesc = findViewById(R.id.txtWarningLongDesc);
    }

    //TODO: add red warning messages
    private void buttonAddDevice(){
        //Add a new device to the database
        btnCreateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //Duration for the warning messages
                    int duration = 2000;
                    Handler handler = new Handler();


                    if(editTxtProducer.getText().toString().isEmpty()){
                        txtWarningProducer.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWarningProducer.setVisibility(View.GONE);
                            }
                        }, duration);
                        Toast.makeText(AddDeviceActivity.this, "Enter devices producer", Toast.LENGTH_SHORT).show();
                    }else if(editTxtModel.getText().toString().isEmpty()){
                        txtWarningModel.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWarningModel.setVisibility(View.GONE);
                            }
                        }, duration);
                        Toast.makeText(AddDeviceActivity.this, "Enter devices model", Toast.LENGTH_SHORT).show();
                    }else if(editTxtBatteryCapacity.getText().toString().isEmpty()){
                        txtWarningBatteryCapacity.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWarningBatteryCapacity.setVisibility(View.GONE);
                            }
                        }, duration);
                        Toast.makeText(AddDeviceActivity.this, "Enter devices capacity", Toast.LENGTH_SHORT).show();
                    } else if(editTxtShortDesc.getText().toString().isEmpty()){
                        txtWarningShortDesc.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWarningShortDesc.setVisibility(View.GONE);
                            }
                        }, duration);
                        Toast.makeText(AddDeviceActivity.this, "Enter devices short description", Toast.LENGTH_SHORT).show();
                    }else if(editTxtLongDesc.getText().toString().isEmpty()){
                        txtWarningLongDesc.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWarningLongDesc.setVisibility(View.GONE);
                            }
                        }, duration);
                        Toast.makeText(AddDeviceActivity.this, "Enter devices long description", Toast.LENGTH_SHORT).show();
                    }else{
                        progressBarAddDevice.setVisibility(View.VISIBLE);

                        Device newDevice = new Device(Integer.parseInt(editTxtBatteryCapacity.getText().toString()), editTxtProducer.getText().toString(), editTxtModel.getText().toString(), editTxtShortDesc.getText().toString(), editTxtLongDesc.getText().toString());

                        //For loading the image url for the new device
                        Utils.getInstance(AddDeviceActivity.this).scrapeImages(newDevice.getProducer() + " " + newDevice.getModel(), imageUrl -> {
                            newDevice.setImgUrl(imageUrl);
                            DeviceDatabase deviceDatabase = new DeviceDatabase(AddDeviceActivity.this);
                            boolean success = deviceDatabase.addDevice(newDevice);

                            //Run the toasts on the main UI thread
                            if (success) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddDeviceActivity.this, "Device added successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddDeviceActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddDeviceActivity.this, "Error working with database", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }catch (Exception e){
                    Toast.makeText(AddDeviceActivity.this, "Error creating device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}