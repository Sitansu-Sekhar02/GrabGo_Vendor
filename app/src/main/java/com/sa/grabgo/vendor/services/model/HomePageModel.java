package com.sa.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomePageModel {
    private final String TAG = "HomePageModel";

    private final String
            WEEKLY                      = "weekly",
            DAILY                       = "daily",
            ORDERS                      = "orders";

    String
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


