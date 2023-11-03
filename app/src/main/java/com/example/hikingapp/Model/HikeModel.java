package com.example.hikingapp.Model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class HikeModel {
    private int ID;
    private String Name;
    private String Location;
    private String Date;
    private int Parking;
    private String Length;
    private String Difficulty;
    private String Description;

    private Bitmap hikeImage;

    public HikeModel(){

    }

    public HikeModel(int ID, String Name, String Location, String Date, int Parking, String Length, String Difficulty, String Description, Bitmap hikeImage){
        this.ID = ID;
        this.Name = Name;
        this.Location = Location;
        this.Date = Date;
        this.Parking = Parking;
        this.Length = Length;
        this.Difficulty = Difficulty;
        this.Description = Description;
        this.hikeImage = hikeImage;
    }

    @Override
    public String toString() {
        return "HikeModel{" +
                "ID= " + ID +
                ", Name='" + Name + '\'' +
                ", Location='" + Location + '\'' +
                ", Date=" + Date +
                ", Parking=" + Parking +
                ", Length=" + Length +
                ", Level='" + Difficulty + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getParking() {
        return Parking;
    }

    public void setParking(int Parking) {
        this.Parking = Parking;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String Length) {
        this.Length = Length;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String Difficulty) {
        this.Difficulty = Difficulty;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Bitmap getHikeImage() {
        return hikeImage;
    }

    public void setHikeImage(Bitmap hikeImage) {
        this.hikeImage = hikeImage;
    }

}
