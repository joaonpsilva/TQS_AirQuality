package com.frstassign.airquality;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Region{
    private String name;
    private HashMap<String,Double> airQual;


    public Region(String name){
        this.name = name;
        this.airQual = new HashMap<>();
    }

    public Region(){
        this.airQual = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAirQual(Map<String,Double> airQual){
        this.airQual=new HashMap<>(airQual);
    }

    public Map<String, Double> getAirQual(){
        return this.airQual;
    }


    public String printAirQual() {
        if (this.airQual.size() == 0)
            return "";
        else if (this.airQual.get("aqi") == -1.0)
            return "Bad Name";
        
        StringBuilder s = new StringBuilder();
        s.append("Air Quality: " + this.airQual.get("aqi"));
        s.append(System.getProperty("line.separator"));

        Iterator it = this.airQual.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (!pair.getKey().equals("aqi")){
                s.append(pair.getKey() + ": " + pair.getValue());
                s.append(System.getProperty("line.separator"));
            }
        }
        return s.toString();
    }

    public String toString(){
        return this.name + " - " + this.airQual.toString();
    }
}