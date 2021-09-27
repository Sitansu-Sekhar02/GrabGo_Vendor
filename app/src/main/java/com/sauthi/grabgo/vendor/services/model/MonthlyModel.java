package com.sauthi.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class MonthlyModel implements Serializable {

    private final String TAG = "MonthlyModel";

    private final String
            TOTAL                      = "total",
            CURRENCY                   = "currency",
            TOTAL_ORDERS               = "total_order";


    private String

            total                   = null,
            currency                = null,
            total_order             = null;


    public MonthlyModel(){
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotal_order() {
        return total_order;
    }

    public void setTotal_order(String total_order) {
        this.total_order = total_order;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(TOTAL))total = json.getString(TOTAL);
            if(json.has(CURRENCY))currency = json.getString(CURRENCY);
            if(json.has(TOTAL_ORDERS))total_order = json.getString(TOTAL_ORDERS);


            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
            return false;}
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(TOTAL, total);
            jsonMain.put(CURRENCY, currency);
            jsonMain.put(TOTAL_ORDERS, total_order);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
