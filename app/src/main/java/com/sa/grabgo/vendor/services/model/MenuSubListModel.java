package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuSubListModel {
    private final String TAG = "MenuSubListModel";

    private final String RESPONSE = "response";

    List<MenuSubModel> menuSubModels = new ArrayList<MenuSubModel>();

    public MenuSubListModel(){}

    public List<MenuSubModel> getMenuSubModels() {
        return menuSubModels;
    }

    public void setMenuSubModels(List<MenuSubModel> menuSubModels) {
        this.menuSubModels = menuSubModels;
    }


/*  public List<String> getNames(){
        List<String> list = new ArrayList<String>();
        for(int i =0 ;i<this.categoryList.size(); i++){
            list.add(categoryList.get(i).getName());
        }
        return list;
    }*/

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            JSONArray array = json.getJSONArray(RESPONSE);
            return this.toObject(array);
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    public boolean toObject(JSONArray jsonArray){
        try{
            List<MenuSubModel> list = new ArrayList<MenuSubModel>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MenuSubModel model = new MenuSubModel();
                model.toObject(jsonObject.toString());
                list.add(model);
            }
            this.menuSubModels = list;
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
            JSONArray jsonArray = new JSONArray();
            List<MenuSubModel> list = this.menuSubModels;
            for(int i=0;i<list.size();i++){
                jsonArray.put(new JSONObject(list.get(i).toString()));
            }
            jsonMain.put(RESPONSE, jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }

    public String toString(boolean isArray){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<MenuSubModel> list = this.menuSubModels;
            for(int i=0;i<list.size();i++){
                jsonArray.put(new JSONObject(list.get(i).toString()));
            }
            if(!isArray){
                jsonMain.put(RESPONSE, jsonArray);
                returnString = jsonMain.toString();
            }else{
                returnString = jsonArray.toString();
            }

        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }
}


