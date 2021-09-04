package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomePageModel {
    private final String TAG = "HomePageModel";

    private final String
            ORDER_ID                            = "order_id",
            RESTAURANT_IMAGE                    = "restaurant_image",
            RESTAURANT_ID                       = "restaurant_id",
            HOME_FILTER_CATEGORY                = "filters",
            TOP_CATEGORY                        = "top_near_rest",
            SUB_CATEGORY                        = "popular_rest";



    String
            order_id            =null,
            restaurant_image    =null,
            restaurant_id    =null;




    public HomePageModel(){}


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRestaurant_image() {
        return restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }



    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            if(json.has(ORDER_ID)){this.order_id = json.getString(ORDER_ID);}
            if(json.has(RESTAURANT_IMAGE)){this.restaurant_image = json.getString(RESTAURANT_IMAGE);}
            if(json.has(RESTAURANT_ID)){this.restaurant_id = json.getString(RESTAURANT_ID);}




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
            jsonMain.put(ORDER_ID, order_id);
            jsonMain.put(RESTAURANT_IMAGE, restaurant_image);
            jsonMain.put(RESTAURANT_ID, restaurant_id);


            //jsonMain.put(PRICE, priceRangeModel!=null ? new JSONObject(priceRangeModel.toString()) : new JSONObject());


            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);
        }
        return returnString;
    }

}


