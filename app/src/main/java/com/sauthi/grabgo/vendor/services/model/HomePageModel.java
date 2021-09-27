package com.sauthi.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class HomePageModel implements Serializable {
    private final String TAG = "HomePageModel";

    private final String
            REST_PREP_TIME              = "rest_prep_time",
            WEEKLY                      = "weekly",
            DAILY                       = "daily",
            ORDERS                      = "orders";

    String
            rest_prep_time           =null,
            weekly                   =null,
            monthly                  =null,
            orders                   =null;


    WeeklyModel
            weeklyModel=null;
    MonthlyModel
            monthlyModel=null;

    OrderListModel
                 orderListModel=null;



    public HomePageModel(){}

    public String getRest_prep_time() {
        return rest_prep_time;
    }

    public void setRest_prep_time(String rest_prep_time) {
        this.rest_prep_time = rest_prep_time;
    }

    public String getWeekly() {
        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public OrderListModel getOrderListModel() {
        return orderListModel;
    }

    public void setOrderListModel(OrderListModel orderListModel) {
        this.orderListModel = orderListModel;
    }

    public WeeklyModel getWeeklyModel() {
        return weeklyModel;
    }

    public void setWeeklyModel(WeeklyModel weeklyModel) {
        this.weeklyModel = weeklyModel;
    }

    public MonthlyModel getMonthlyModel() {
        return monthlyModel;
    }

    public void setMonthlyModel(MonthlyModel monthlyModel) {
        this.monthlyModel = monthlyModel;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);

            if (json.has(REST_PREP_TIME)) {
                rest_prep_time = json.getString(REST_PREP_TIME);
            }
            if(json.has(WEEKLY)){
                WeeklyModel weeklyModel1 = new WeeklyModel();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = json.getJSONObject(WEEKLY);
                if(jsonObject1 != null){weeklyModel1.toObject(jsonObject1.toString());}
                weeklyModel = weeklyModel1;
            }

            if(json.has(DAILY)){
                MonthlyModel monthlyModel1 = new MonthlyModel();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = json.getJSONObject(DAILY);
                if(jsonObject1 != null){monthlyModel1.toObject(jsonObject1.toString());}
                monthlyModel = monthlyModel1;
            }

            if(json.has(ORDERS)) {
                JSONArray array = json.getJSONArray(ORDERS);
                OrderListModel listModelLocal = new OrderListModel();
                listModelLocal.setRESPONSE(ORDERS);
                if(listModelLocal.toObject(array)){this.orderListModel = listModelLocal;}
                else{this.orderListModel = null;}
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
            jsonMain.put(REST_PREP_TIME, rest_prep_time);
            jsonMain.put(WEEKLY, weekly);
            jsonMain.put(DAILY, monthly);
            jsonMain.put(ORDERS, orderListModel!=null?new JSONArray(orderListModel.toString(true)):null);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);
        }
        return returnString;
    }

}


