package com.sa.grabgo.vendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public static final String TAG = "HomeAdapter";

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_order_time,tv_total_item,tv_cancel_order,tv_confirm_order;
        RecyclerView rv_order_details;
        LinearLayout ll_view_order;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_total_item = itemView.findViewById(R.id.tv_total_item);
            tv_cancel_order = itemView.findViewById(R.id.tv_cancel_order);
            tv_confirm_order = itemView.findViewById(R.id.tv_confirm_order);
            rv_order_details = itemView.findViewById(R.id.rv_order_details);
            ll_view_order = itemView.findViewById(R.id.ll_view_order);

        }
    }
}
