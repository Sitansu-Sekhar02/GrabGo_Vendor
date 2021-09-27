package com.sauthi.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class CategoryMainModel implements Serializable {
    private final String TAG = "CategoryMainModel";
    private final String
            RESPONSE            = "response",
            STATUS              = "status_bool",
            MESSAGE             = "status";

    CategoryListModel
            categoryListModel      = null;


    String message = null;
    boolean isStatus=false;

    public CategoryMainModel(){}


    public CategoryListModel getCategoryListModel() {
        return categoryListModel;
    }

    public void setCategoryListModel(CategoryListModel categoryListModel) {
        this.categoryListModel = categoryListModel;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            if(json.has(MESSAGE)){this.message = json.getString(MESSAGE);}
            if(json.has(STATUS)){this.isStatus = json.getBoolean(STATUS);}

            if(json.has(RESPONSE)){
                CategoryListModel orderDetailListModel = new CategoryListModel();
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray(RESPONSE);
                if(jsonArray!=null){orderDetailListModel.toObject(jsonArray);}
                this.categoryListModel = orderDetailListModel;
            }

            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(STATUS, isStatus);
            jsonMain.put(MESSAGE, message);
            jsonMain.put(RESPONSE, this.categoryListModel != null? new JSONArray(this.categoryListModel.toString(true)) : new JSONArray());

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
