package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDeviceActivity extends AppCompatActivity {
    private EditText editTxtProducer, editTxtModel, editTxtBatteryCapacity, editTxtShortDesc, editTxtLongDesc;
    private Button btnCreateDevice;
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
    }

    private void buttonAddDevice(){
        //Add a new device to the database
        btnCreateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(editTxtBatteryCapacity.getText().toString().isEmpty()){
                        Toast.makeText(AddDeviceActivity.this, "Enter devices capacity", Toast.LENGTH_SHORT).show();
                    }else if(editTxtProducer.getText().toString().isEmpty()){
                        Toast.makeText(AddDeviceActivity.this, "Enter devices producer", Toast.LENGTH_SHORT).show();
                    }else if(editTxtModel.getText().toString().isEmpty()){
                        Toast.makeText(AddDeviceActivity.this, "Enter devices model", Toast.LENGTH_SHORT).show();
                    }else if(editTxtShortDesc.getText().toString().isEmpty()){
                        Toast.makeText(AddDeviceActivity.this, "Enter devices short description", Toast.LENGTH_SHORT).show();
                    }else if(editTxtLongDesc.getText().toString().isEmpty()){
                        Toast.makeText(AddDeviceActivity.this, "Enter devices long description", Toast.LENGTH_SHORT).show();
                    }else{
                        Device newDevice = new Device(Integer.parseInt(editTxtBatteryCapacity.getText().toString()), editTxtProducer.getText().toString(), editTxtModel.getText().toString(), editTxtShortDesc.getText().toString(), editTxtLongDesc.getText().toString());

                        //For loading the image url for the new device
                        Utils.getInstance(AddDeviceActivity.this).scrapeImages(newDevice.getProducer() + " " + newDevice.getModel(), imageUrl -> {
                            newDevice.setImgUrl(imageUrl);
                            DeviceDatabase deviceDatabase = new DeviceDatabase(AddDeviceActivity.this);
                            boolean success = deviceDatabase.addDevice(newDevice);
                            if(success){
                                Toast.makeText(AddDeviceActivity.this, "Device added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddDeviceActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(AddDeviceActivity.this, "Error working with database", Toast.LENGTH_SHORT).show();
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