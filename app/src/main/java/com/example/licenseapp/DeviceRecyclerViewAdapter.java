package com.example.licenseapp;

import static android.content.Context.MODE_PRIVATE;
import static com.example.licenseapp.DeviceActivity.DEVICE_ID_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class DeviceRecyclerViewAdapter extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder> {
    private List<Device> devices = new ArrayList<>();
    private Context context;

    public DeviceRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(devices.get(position).getImgUrl()).into(holder.imgDevice);
        holder.txtDeviceName.setText(devices.get(position).getDeviceTitle());
        holder.txtBatteryCapacity.setText(String.valueOf(devices.get(position).getBattery()) + " mAh");
        holder.txtShortDesc.setText(devices.get(position).getShortDesc());
        holder.txtChargeTime.setText(deviceChargingTime(devices.get(position)));

        //Navigate to each device page
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, DeviceActivity.class);
                    intent.putExtra(DEVICE_ID_KEY,devices.get(clickedPosition).getId());
                    context.startActivity(intent);
                }
            }
        });

        //Expand or collapse the card view by pressing the arrows and hide the down arrow
        // Expand or collapse the card view by pressing the arrows
        if (devices.get(position).isExpanded()) {
            holder.expandedRelativeLayout.setVisibility(View.VISIBLE);
            holder.btnDownArrow.setVisibility(View.GONE);
            holder.btnUpArrow.setVisibility(View.VISIBLE);
        } else {
            holder.expandedRelativeLayout.setVisibility(View.GONE);
            holder.btnDownArrow.setVisibility(View.VISIBLE);
            holder.btnUpArrow.setVisibility(View.GONE);
        }
    }

    private String deviceChargingTime(Device device){
        // Retrieve the voltage from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        float voltage = prefs.getFloat("VOLTAGE", 0.0f);

        //Let's say if we have 5 volts we have 1000 mah (the maximum ammount of intensity(mAh))
        float intensity = voltage * 250;

        float chargingTime = (float )device.getBattery() / intensity;

        //Format the charging time to display only two decimal places
        String formattedChargingTime = String.format("%.2f", chargingTime);

        return formattedChargingTime + " hours";
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<Device> devices){
        this.devices = devices;
        notifyDataSetChanged();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        private CardView parent;
        private ImageView imgDevice, btnDownArrow, btnUpArrow;
        private TextView txtDeviceName, txtBatteryCapacity, txtChargeTime, txtShortDesc;
        private RelativeLayout expandedRelativeLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            initViews(itemView);
            collapseExpandViewCard();
        }

        private void initViews(View itemView){
            parent = itemView.findViewById(R.id.parent);
            imgDevice = itemView.findViewById(R.id.imgDevice);
            btnDownArrow = itemView.findViewById(R.id.btnDownArrow);
            btnUpArrow = itemView.findViewById(R.id.btnUpArrow);
            txtDeviceName = itemView.findViewById(R.id.txtDeviceName);
            txtBatteryCapacity = itemView.findViewById(R.id.txtBatteryCapacity);
            txtChargeTime = itemView.findViewById(R.id.txtChargeTime);
            txtShortDesc = itemView.findViewById(R.id.txtShortDesc);
            expandedRelativeLayout = itemView.findViewById(R.id.expandedRelativeLayout);
        }

        private void collapseExpandViewCard(){
            btnDownArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = devices.get(getAdapterPosition());
                    device.setExpanded(!device.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            btnUpArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = devices.get(getAdapterPosition());
                    device.setExpanded(!device.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}