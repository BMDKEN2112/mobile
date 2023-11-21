package com.example.hikingapp.Model;

import android.graphics.Bitmap;

public class ObservationModel {
    private int observationID;

    private String observationName;

    private String observationTime;

    private String observationComment;

    private int hikeID;

    private Bitmap observationImage;

    public ObservationModel(int observationID, String observationName, String observationTime, String observationComment, int hikeID, Bitmap observationImage)
    {
        this.observationID = observationID;
        this.observationName = observationName;
        this.observationTime = observationTime;
        this.observationComment = observationComment;
        this.hikeID = hikeID;
        this.observationImage = observationImage;
    }

    public ObservationModel(){

    }


    public int getObservationID(){
        return observationID;
    }

    public void setObservationID(int observationID){
        this.observationID = observationID;
    }

    public String getObservationName(){
        return observationName;
    }

    public void setObservationName(String observationName){
        this.observationName = observationName;
    }

    public String getObservationTime(){
        return observationTime;
    }

    public void setObservationTime(String observationTime){
        this.observationTime = observationTime;
    }

    public String getObservationComment(){
        return observationComment;
    }

    public void setObservationComment(String observationComment){
        this.observationComment = observationComment;
    }

    public int getHikeID(){
        return hikeID;
    }

    public void setHikeID(int hikeID){
        this.hikeID = hikeID;
    }

    public Bitmap getObservationImage(){
        return observationImage;
    }

    public void setObservationImage(Bitmap observationImage){
        this.observationImage = observationImage;
    }

}
