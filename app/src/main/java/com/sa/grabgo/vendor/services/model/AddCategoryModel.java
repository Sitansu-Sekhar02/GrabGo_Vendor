package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class AddCategoryModel implements Serializable {

    private final String TAG = "AddCategoryModel";

    private final String
                    NAME               = "name",
                    AR_NAME            = "ar_name";


    private  String
            name                       = null,
            ar_name                    = null;




    public AddCategoryModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);

            if (json.has(NAME)) {
                name= json.getString(NAME);
            }
            if (json.has(AR_NAME)) {
                ar_name= json.getString(AR_NAME);
            }


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
            jsonMain.put(NAME, name);
            jsonMain.put(AR_NAME, ar_name);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }

}
