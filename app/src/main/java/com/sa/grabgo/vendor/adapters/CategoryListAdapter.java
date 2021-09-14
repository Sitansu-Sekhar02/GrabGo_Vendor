package com.sa.grabgo.vendor.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.menu.MenuSubListActivity;
import com.sa.grabgo.vendor.services.model.CategoryModel;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    public static final String TAG = "MenuAdapter";

    private final List<CategoryModel> list;
    private final Activity activity;

    public CategoryListAdapter(Activity activity, List<CategoryModel> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_type_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {

        final CategoryModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getName())){
            holder.tv_item_name.setText(model.getName());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name,tv_available_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_item_name = itemView.findViewById(R.id.tv_item_name);

        }
    }
}
