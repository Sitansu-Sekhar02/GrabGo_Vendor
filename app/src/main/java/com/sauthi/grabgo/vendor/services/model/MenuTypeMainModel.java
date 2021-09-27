package com.sauthi.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class MenuTypeMainModel implements Serializable {
    private final String TAG = "MenuTypeMainModel";
    private final String
            RESPONSE            = "response",
            STATUS              = "status_bool",
            MESSAGE             = "status";

    MenuTypeListModel
            menuTypeListModel = null;


    String message = null;
    boolean isStatus=false;

    public MenuTypeMainModel(){}


    public MenuTypeListModel getMenuTypeListModel() {
        return menuTypeListModel;
    }

    public void setMenuTypeListModel(MenuTypeListModel menuTypeListModel) {
        this.menuTypeListModel = menuTypeListModel;
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
                MenuTypeListModel menuTypeListModel1 = new MenuTypeListModel();
                JSONArray jsonArray = new JSONArray();
                jsonArray = json.getJSONArray(RESPONSE);
                if(jsonArray!=null){
                    menuTypeListModel1.toObject(jsonArray);}
                this.menuTypeListModel = menuTypeListModel1;
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
            jsonMain.put(RESPONSE, this.menuTypeListModel != null? new JSONArray(this.menuTypeListModel.toString(true)) : new JSONArray());

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
