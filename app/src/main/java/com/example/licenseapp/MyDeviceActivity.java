package com.example.licenseapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyDeviceActivity extends AppCompatActivity {
    private TextView txtBrand, txtModel, txtMyDeviceBatteryCapacity, txtChargingTime;
    private ImageView imgMyDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device);

        initViews();
        setViews();
    }

    private void initViews() {
        txtBrand = findViewById(R.id.txtBrand);
        txtModel = findViewById(R.id.txtModel);
        txtMyDeviceBatteryCapacity = findViewById(R.id.txtMyDeviceBatteryCapacity);
        txtChargingTime = findViewById(R.id.txtChargingTime);
        imgMyDevice = findViewById(R.id.imgMyDevice);
    }

    private void setViews(){
        Glide.with(this).asBitmap().load(Utils.getInstance(this).getMyDeviceImgUrl()).into(imgMyDevice);
        txtBrand.setText(Build.BRAND.toUpperCase());
        txtModel.setText(Build.MODEL);
        txtMyDeviceBatteryCapacity.setText(String.valueOf(getBatteryCapacity(this)) + " mAh");
        txtChargingTime.setText("N/A");
    }

    private double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;
    }
}