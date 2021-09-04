package com.sa.grabgo.vendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;

public class HomeSubListAdapter extends RecyclerView.Adapter<HomeSubListAdapter.ViewHolder> {

    public static final String TAG = "HomeSubListAdapter";

    @NonNull
    @Override
    public HomeSubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSubListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name,tv_currency,tv_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_currency = itemView.findViewById(R.id.tv_currency);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);

        }
    }
}
