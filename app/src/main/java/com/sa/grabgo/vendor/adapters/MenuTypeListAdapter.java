package com.sa.grabgo.vendor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.services.model.MenuTypeModel;
import com.sa.grabgo.vendor.services.model.OrderDetailModel;

import java.util.List;

public class MenuTypeListAdapter extends RecyclerView.Adapter<MenuTypeListAdapter.ViewHolder> {

    public static final String TAG = "MenuTypeListAdapter";


    private final List<MenuTypeModel> list;
    private final Activity activity;

    public MenuTypeListAdapter(Activity activity, List<MenuTypeModel> list) {
        this.list = list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public MenuTypeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_type_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuTypeListAdapter.ViewHolder holder, int position) {


        final MenuTypeModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getCount())){
            holder.tv_item_name.setText(model.getCount());
        }

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
