package com.sa.grabgo.vendor.adapters.interfaces;


import com.sa.grabgo.vendor.services.model.OrderModel;

public interface UpdateCurrentStatusInvoke {
    void OnItemReady(OrderModel orderModel, String from, String order_id);
    void OnItemDelivered(OrderModel orderModel, String from, String order_id);
}
