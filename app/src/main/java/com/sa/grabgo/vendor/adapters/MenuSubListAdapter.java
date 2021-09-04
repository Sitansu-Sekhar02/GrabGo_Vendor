package com.sa.grabgo.vendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;

public class MenuSubListAdapter extends RecyclerView.Adapter<MenuSubListAdapter.ViewHolder> {

    public static final String TAG = "MenuSubListAdapter";

    @NonNull
    @Override
    public MenuSubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_sub_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuSubListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_tv,tv_currency,unit_price_tv,product_description_tv;
        ImageView  product_iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_title_tv = itemView.findViewById(R.id.item_title_tv);
            tv_currency = itemView.findViewById(R.id.tv_currency);
            unit_price_tv = itemView.findViewById(R.id.unit_price_tv);
            product_description_tv = itemView.findViewById(R.id.product_description_tv);
            product_iv = itemView.findViewById(R.id.product_iv);

        }
    }
}
