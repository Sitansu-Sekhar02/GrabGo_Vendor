package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class MenuTypeModel implements Serializable {
    private final String TAG = "MenuTypeModel";
    private final String
            MENU_ID              = "id",
            NAME                 = "name",
            COUNT                = "count";

    String
            menu_id              = null,
            name                 = null,
            count                = null;

    public MenuTypeModel(){}

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(MENU_ID))this.menu_id = json.getString(MENU_ID);
            if(json.has(NAME))this.name = json.getString(NAME);
            if(json.has(COUNT))this.count = json.getString(COUNT);

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
            jsonMain.put(MENU_ID, menu_id);
            jsonMain.put(NAME, name);
            jsonMain.put(COUNT, count);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}
