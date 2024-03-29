package com.example.licenseapp;

public class GraphModel {
    private int id;
    private float energy;
    private String date;

    public GraphModel(int id, String date, float energy) {
        this.id = id;
        this.date = date;
        this.energy = energy;
    }

    public GraphModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GraphModel{" +
                "id=" + id +
                ", energy=" + energy +
                ", date='" + date + '\'' +
                '}';
    }
}
