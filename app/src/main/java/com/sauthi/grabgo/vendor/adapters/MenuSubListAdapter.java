package com.sauthi.grabgo.vendor.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sauthi.grabgo.vendor.AppController;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.menu.AddItemActivity;
import com.sauthi.grabgo.vendor.services.model.MenuSubModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuSubListAdapter extends RecyclerView.Adapter<MenuSubListAdapter.ViewHolder> {

    public static final String TAG = "MenuSubListAdapter";

    private final List<MenuSubModel> list;
    private final Activity activity;
    GlobalFunctions globalFunctions;

    public MenuSubListAdapter(Activity activity, List<MenuSubModel> list) {
        this.list = list;
        this.activity = activity;
        globalFunctions = AppController.getInstance().getGlobalFunctions();

    }


    @NonNull
    @Override
    public MenuSubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_sub_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuSubListAdapter.ViewHolder holder, int position) {
        final MenuSubModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getName())){
            holder.item_title_tv.setText(model.getName());
        }
        if (GlobalFunctions.isNotNullValue(model.getCurrency())) {
            holder.tv_currency.setText(model.getCurrency());
        }
        if (GlobalFunctions.isNotNullValue(model.getPrice())) {
            holder.unit_price_tv.setText(model.getPrice());
        }
        if (GlobalFunctions.isNotNullValue(model.getDescription())) {
            holder.product_description_tv.setText(globalFunctions.getHTMLString(model.getDescription()));

        }
        if (GlobalFunctions.isNotNullValue(model.getImage())) {
            Picasso.with(activity).load(model.getImage()).placeholder(R.drawable.ic_lazyload).into(holder.product_iv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddItemActivity.newInstance(activity, model);
                activity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
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
