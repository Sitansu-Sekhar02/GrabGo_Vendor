package com.sauthi.grabgo.vendor.adapters.interfaces;


import com.sauthi.grabgo.vendor.services.model.OrderModel;

public interface UpdateCurrentStatusInvoke {
    void OnItemReady(OrderModel orderModel, String from, String order_id);
    void OnItemDelivered(OrderModel orderModel, String from, String order_id);
}
