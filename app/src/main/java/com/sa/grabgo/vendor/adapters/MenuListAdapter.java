package com.sa.grabgo.vendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.R;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    public static final String TAG = "MenuAdapter";

    @NonNull
    @Override
    public MenuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
