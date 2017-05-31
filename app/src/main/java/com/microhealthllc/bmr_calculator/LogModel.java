package com.microhealthllc.bmr_calculator;

/**
 * Created by dan on 3/20/17.
 */


public class LogModel {
    private String bmi, weight, datetime, calories_needed;

    public LogModel() {
    }

    public LogModel(String bmi, String weight, String datetime) {
        this.bmi= bmi;
        this.weight = weight;
        this.datetime = datetime;
    }

    public LogModel(String bmi, String weight,String calories_needed, String datetime) {
        this.bmi= bmi;
        this.weight = weight;
        this.calories_needed = calories_needed;
        this.datetime = datetime;

    }
    public String getBmi() {
        return  bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }
    public void  setCalories_needed(String calories_needed)
    {
    this.calories_needed = calories_needed;
}
public String getCalories_needed(){
    return this.calories_needed;
}
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}