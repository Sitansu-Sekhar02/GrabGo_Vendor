package com.sa.grabgo.vendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.ViewHolder> {

    public static final String TAG = "PastOrderAdapter";

    @NonNull
    @Override
    public PastOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.past_order_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_order_time,tv_total_item,tv_order_delivery;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_total_item = itemView.findViewById(R.id.tv_total_item);
            tv_order_delivery = itemView.findViewById(R.id.tv_order_delivery);

        }
    }
}
