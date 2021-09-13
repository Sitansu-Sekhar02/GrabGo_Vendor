package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class MenuSubMainModel implements Serializable {
    private final String TAG = "MenuSubMainModel";
    private final String
            RESPONSE            = "response",
            STATUS              = "status_bool",
            MESSAGE             = "status";

    MenuSubListModel
            menuSubListModel      = null;


    String message = null;
    boolean isStatus=false;

    public MenuSubMainModel(){}


    public MenuSubListModel getMenuSubListModel() {
        return menuSubListModel;
    }

    public void setMenuSubListModel(MenuSubListModel menuSubListModel) {
        this.menuSubListModel = menuSubListModel;
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
                MenuSubListModel orderDetailListModel = new MenuSubListModel();
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray(RESPONSE);
                if(jsonArray!=null){orderDetailListModel.toObject(jsonArray);}
                this.menuSubListModel = orderDetailListModel;
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
            jsonMain.put(RESPONSE, this.menuSubListModel != null? new JSONArray(this.menuSubListModel.toString(true)) : new JSONArray());

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
