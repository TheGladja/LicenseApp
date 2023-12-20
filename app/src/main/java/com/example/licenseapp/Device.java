package com.example.licenseapp;

public class Device {
    private static int nextId = 1;
    private int battery, id;
    private String producer, model, imgUrl, shortDesc, longDesc;
    private boolean isExpanded;

    public Device(int battery, String producer, String model, String imgUrl, String shortDesc, String longDesc) {
        //Increment id by 1 for each new device
        this.id = nextId;
        nextId++;
        this.battery = battery;
        this.producer = producer;
        this.model = model;
        this.imgUrl = imgUrl;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        //Initially the card view is not expanded
        isExpanded = false;
    }

    public int getBattery() {
        return battery;
    }

    public int getId() {
        return id;
    }

    public String getProducer() {
        return producer;
    }

    public String getModel() {
        return model;
    }

    public String getDeviceTitle(){
        return producer + " " + model;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "Device{" +
                "battery=" + battery +
                ", id=" + id +
                ", producer='" + producer + '\'' +
                ", model='" + model + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
