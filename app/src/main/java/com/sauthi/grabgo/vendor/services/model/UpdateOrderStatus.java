package com.sauthi.grabgo.vendor.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class UpdateOrderStatus implements Serializable {

    private final String TAG = "UpdateOrderStatus";

    private final String
            STATUS          = "status",
            ORDER_ID        = "order_id";

    private String
            status       = null,
            order_id     = null;


    public UpdateOrderStatus(){}

    List<UpdateOrderStatus> statusList = new ArrayList<UpdateOrderStatus>();

    public List<UpdateOrderStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<UpdateOrderStatus> statusList) {
        this.statusList = statusList;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(STATUS))this.status = json.getString(STATUS);
            if(json.has(ORDER_ID))this.order_id = json.getString(ORDER_ID);


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
            jsonMain.put(STATUS, status);
            jsonMain.put(ORDER_ID, order_id);

            returnString = jsonMain.toString();
        }
        catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }

}
