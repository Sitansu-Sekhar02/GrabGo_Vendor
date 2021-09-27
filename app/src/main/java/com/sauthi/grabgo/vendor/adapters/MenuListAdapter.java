package com.sauthi.grabgo.vendor.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.menu.MenuSubListActivity;
import com.sauthi.grabgo.vendor.services.model.CategoryModel;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    public static final String TAG = "MenuListAdapter";

    private final List<CategoryModel> list;
    private final Activity activity;

    public MenuListAdapter(Activity activity, List<CategoryModel> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MenuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListAdapter.ViewHolder holder, int position) {

        final CategoryModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getName())){
            holder.tv_menu_title.setText(model.getName());
        }
        if (GlobalFunctions.isNotNullValue(model.getCount())) {
            holder.tv_available_qty.setText(model.getCount());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MenuSubListActivity.newInstance(activity, model);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_menu_title,tv_available_qty;
        LinearLayout ll_menu_listMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_menu_title = itemView.findViewById(R.id.tv_menu_title);
            tv_available_qty = itemView.findViewById(R.id.tv_available_qty);
            ll_menu_listMain = itemView.findViewById(R.id.ll_menu_listMain);

        }
    }
}
