package com.sa.grabgo.vendor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.services.model.OrderDetailModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeSubListAdapter extends RecyclerView.Adapter<HomeSubListAdapter.ViewHolder> {

    public static final String TAG = "HomeSubListAdapter";


    private final List<OrderDetailModel> list;
    private final Activity activity;

    public HomeSubListAdapter(Activity activity, List<OrderDetailModel> list) {
        this.list = list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public HomeSubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSubListAdapter.ViewHolder holder, int position) {


        final OrderDetailModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getName()) && (GlobalFunctions.isNotNullValue(model.getQuantity()))){
            holder.tv_item_name.setText(model.getName()+" x "+(model.getQuantity()));
        }
        /*if (GlobalFunctions.isNotNullValue(model.getCurrency())) {
            holder.tv_currency.setText(model.getCurrency());
        }
        if (GlobalFunctions.isNotNullValue(model.getPrice())) {
            holder.tv_total_price.setText(model.getPrice());
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
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
