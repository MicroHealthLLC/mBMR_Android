package com.microhealthllc.bmr_calculator.DB;

/**
 * Created by ubuntuadmin on 3/13/17.
 */

public class BmiLogs {
    private int id;
    private String bmi;
    private String weight;
    private String datetime;
    private String calories_needed;

    public BmiLogs() {
    }

    public BmiLogs(int id,String bmi,String weight,String datetime)
    {
        this.id=id;
        this.bmi=bmi;
        this.weight=weight;
        this.datetime =datetime;
    }

    public BmiLogs(String bmi,String weight, String calories_needed, String datetime)
    {

        this.bmi=bmi;
        this.weight=weight;
        this.calories_needed = calories_needed;
        this.datetime =datetime;

    }
    public void setId(int id) {
        this.id = id;
    }
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }
    public void setCalories_needed(String calories_needed){
        this.calories_needed = calories_needed;
    }
    public String getCalories_needed(){
        return this.calories_needed;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public void setDateTime(String datetime){
        this.datetime = datetime;
    }
    public int getId() {
        return id;
    }
    public String getBmi() {
        return bmi;
    }
    public String getWeight() {
        return weight;
    }
    public String getDateTime() {
       return datetime;
    }
}

