package com.sauthi.grabgo.vendor.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.adapters.interfaces.UpdateCurrentStatusInvoke;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.orders.CurrentOrderDetailsActivity;
import com.sauthi.grabgo.vendor.services.model.OrderDetailModel;
import com.sauthi.grabgo.vendor.services.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.ViewHolder> {

    public static final String TAG = "CurrentOrderAdapter";

    private final List<OrderModel> list;
    private final Activity activity;
    HomeSubListAdapter homeSubListAdapter;
    UpdateCurrentStatusInvoke updateCurrentStatusInvoke;
    String pageFrom = null;
    String order_id = null;

    public CurrentOrderAdapter(Activity activity, List<OrderModel> list,UpdateCurrentStatusInvoke updateCurrentStatusInvoke) {
        this.list = list;
        this.activity = activity;
        this.updateCurrentStatusInvoke = updateCurrentStatusInvoke;

    }

    @NonNull
    @Override
    public CurrentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.current_order_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderAdapter.ViewHolder holder, int position) {

        final OrderModel model = list.get(position);

        if (GlobalFunctions.isNotNullValue(model.getOrder_id())) {
            holder.tv_order_id.setText(model.getOrder_id());
        }

        if (GlobalFunctions.isNotNullValue(model.getCreated_on())) {
            holder.tv_order_time.setText(GlobalFunctions.getDateFormat(model.getCreated_on()));
        }

        if (GlobalFunctions.isNotNullValue(model.getCounts())) {
            holder.tv_total_item.setText(model.getCounts());
        }
       /* if (GlobalFunctions.isNotNullValue(model.getRest_preparation_time())){
            FragmentOrderMain.tv_est_time.setText(model.getRest_preparation_time());
        }*/

        if (GlobalFunctions.isNotNullValue(model.getOrder_id())){
            order_id=model.getOrder_id();
        }


        holder.tv_order_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageFrom = GlobalVariables.ORDER_READY;

                updateCurrentStatusInvoke.OnItemReady(model, pageFrom,order_id);
                notifyDataSetChanged();

            }
        });
        holder.tv_order_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageFrom = GlobalVariables.ORDER_COMPLETED;

                updateCurrentStatusInvoke.OnItemDelivered(model, pageFrom,order_id);
                notifyDataSetChanged();
            }
        });



        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        List<OrderDetailModel> productList = new ArrayList<OrderDetailModel>();

        if (model.getOrder_details() != null && model.getOrder_details().getOrderDetailModels().size() > 0) {
            productList.clear();
            productList.addAll(model.getOrder_details().getOrderDetailModels());
        }

        if (productList.size() > 0) {
            homeSubListAdapter = new HomeSubListAdapter(activity, productList);
            holder.rv_order_details.setLayoutManager(layoutManager);
            holder.rv_order_details.setAdapter(homeSubListAdapter);

            holder.rv_order_details.setVisibility(View.VISIBLE);
        } else {
            holder.rv_order_details.setVisibility(View.GONE);
        }

        holder.ll_view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CurrentOrderDetailsActivity.newInstance(activity, model);
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_order_time,tv_total_item,tv_order_delivery,tv_order_ready;
        RecyclerView rv_order_details;
        CircularProgressBar progressBar;
        LinearLayout ll_view_order;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_total_item = itemView.findViewById(R.id.tv_total_item);
            tv_order_delivery = itemView.findViewById(R.id.tv_order_delivery);
            tv_order_ready = itemView.findViewById(R.id.tv_order_ready);
            progressBar = itemView.findViewById(R.id.progress_bar);
            rv_order_details = itemView.findViewById(R.id.rv_order_details);
            ll_view_order = itemView.findViewById(R.id.ll_view_order);

        }
    }
}
