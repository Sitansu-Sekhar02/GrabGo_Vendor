package com.sauthi.grabgo.vendor.adapters.interfaces;


import com.sauthi.grabgo.vendor.services.model.OrderModel;

public interface UpdateStatusInterface {
    void OnCancelClickListener(OrderModel orderModel, String from, String order_id);
    void OnConfirmClickListener(OrderModel orderModel, String from, String order_id);
}
