package com.example.licenseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    private static Utils instance;
    private SharedPreferences sharedPreferences;
    private static final String ALL_DEVICES_KEY = "all_devices", MY_DEVICE_IMG_URL = "my_device_img_url";

    private Utils(Context context){
        sharedPreferences = context.getSharedPreferences("simple_db", Context.MODE_PRIVATE);

        if(getAllDevices() == null){
            //If it is the first time the user launches the app
            //It will initialize the data
            initData();
        }
        initData();
    }

    private void initData() {
        ArrayList<Device> devices = new ArrayList<>();

        initDevices(devices);

        //For loading each the image url for each device in the array
        //We use AtomicInteger in order to avoid any issue counting the number of devices(with uploaded image urls) in this multi threaded environment
        AtomicInteger imageLoadedCount = new AtomicInteger(0);

        for (Device device : devices) {
            scrapeImages(device.getDeviceTitle(), imageUrl -> {
                device.setImgUrl(imageUrl);

                // Increment the loaded image count
                int count = imageLoadedCount.incrementAndGet();

                // Check if all images are loaded
                if (count == devices.size()) {
                    // All images loaded, persist the updated data
                    persistDevicesData(devices);
                }
            });
        }

        //For loading the image url for my device
        scrapeImages(Build.BRAND + " " + Build.MODEL, imageUrl -> {
            persistMyDeviceImageUrl(imageUrl);
        });
    }

    private void initDevices(ArrayList<Device> devices){
        devices.add(new Device(
                2000,
                "Samsung",
                "galaxy s21",
                "image",
                "A world of pure wilderness",
                "Long descriptionLong descriptionLong descriptionLong descriptionLong descriptionLong descriptionLong descriptionLong description" +
                        "Long descriptionLong descriptionLong descriptionLong descriptionLong descriptionLong descriptionLong description" +
                        "Long descriptionLong descriptionLong descriptionLong descriptionLong description" +
                        "Long descriptionLong descriptionLong descriptionLong descriptionLong description"
        ));

        devices.add(new Device(700, "Iphone", "15s", "image",
                "Full of horror and suspense", "Long description"));
        devices.add(new Device(400,"Alcatel", "galaxy s21", "image",
                "A world of pure wilderness", "Long description"));
        devices.add(new Device(1000, "Xiaomi", "15s", "image",
                "Full of horror and suspense", "Long description"));
    }

    private void persistDevicesData(ArrayList<Device> devices) {
        // Persist the data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_DEVICES_KEY, gson.toJson(devices));
        //editor.putString(MY_DEVICE_IMG_URL, gson.toJson(myDeviceImgUrl));
        editor.apply(); // Use apply() for asynchronous write
    }

    private void persistMyDeviceImageUrl(String imageUrl){
        // Persist the data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(MY_DEVICE_IMG_URL, gson.toJson(imageUrl));
        editor.apply(); // Use apply() for asynchronous write
    }

    public static Utils getInstance(Context context) {
        if(instance == null)
            instance = new Utils(context);
        return instance;
    }

    public ArrayList<Device> getAllDevices() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Device>>(){}.getType();
        ArrayList<Device> devices = gson.fromJson(sharedPreferences.getString(ALL_DEVICES_KEY, null), type);
        return devices;
    }

    public String getMyDeviceImgUrl(){
        Gson gson = new Gson();
        Type type = new TypeToken<String>(){}.getType();
        String imageUrl = gson.fromJson(sharedPreferences.getString(MY_DEVICE_IMG_URL, null), type);
        return imageUrl;
    }

    public Device getdeviceById(int id){
        //Get the device from SharedPreferences (like a database)
        ArrayList<Device> devices = getAllDevices();
        if(devices != null){
            for(Device d : devices){
                if(d.getId() == id)
                    return d;
            }
        }
        return null;
    }

    //CALLBACK INTERFACE
    //We are using this to get notified when an image url has been found
    public interface ImageLoadListener {
        void onImageLoadComplete(String imageUrl);
    }

    //We use listener as a parameter along with starting a new thread in order to make this method async
    //When listener.onImageLoadComplete(imageUrl) will be called we will know that the image url has ben successfully selected
    public static void scrapeImages(final String searchQuery, final ImageLoadListener listener) {
        new Thread(() -> {
            ArrayList<String> imageUrls = new ArrayList<>();

            try {
                String searchUrl = "https://www.google.com/search?q=" + searchQuery + "&tbm=isch";
                Document doc = Jsoup.connect(searchUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                        .get();

                Elements imageElements = doc.select("img");
                for (Element imageElement : imageElements) {
                    String imageUrl = imageElement.attr("src");
                    // Check if the URL is a valid image URL
                    if (imageUrl.startsWith("http")) {
                        imageUrls.add(imageUrl);
                    }
                }

                if (!imageUrls.isEmpty() && listener != null) {
                    listener.onImageLoadComplete(imageUrls.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
