package com.example.licenseapp;

public class GraphModel {
    private int id, energy;
    private String date;

    public GraphModel(int id, String date, int energy) {
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

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
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
