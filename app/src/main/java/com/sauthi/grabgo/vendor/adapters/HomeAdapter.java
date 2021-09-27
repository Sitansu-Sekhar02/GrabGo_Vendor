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

import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.adapters.interfaces.UpdateStatusInterface;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.orders.OrderDetailsActivity;
import com.sauthi.grabgo.vendor.services.model.OrderDetailModel;
import com.sauthi.grabgo.vendor.services.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public static final String TAG = "HomeAdapter";

    private final List<OrderModel> list;
    private final Activity activity;
    HomeSubListAdapter homeSubListAdapter;
    UpdateStatusInterface updateStatusInterface;
    int selectedPos = -1, clickedPos = -1;
    String pageFrom = null;
    String order_id = null;


    public HomeAdapter(Activity activity, List<OrderModel> list, UpdateStatusInterface updateStatusInterface) {
        this.list = list;
        this.activity = activity;
        this.updateStatusInterface = updateStatusInterface;

    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {

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
        if (GlobalFunctions.isNotNullValue(model.getOrder_id())){
            order_id=model.getOrder_id();
        }


        holder.tv_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageFrom = GlobalVariables.ORDER_VENDOR_CANCELLED;

                updateStatusInterface.OnCancelClickListener(model, pageFrom,order_id);
                notifyDataSetChanged();

            }
        });
        holder.tv_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageFrom = GlobalVariables.ORDER_CONFIRMED;

                updateStatusInterface.OnConfirmClickListener(model, pageFrom,order_id);
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
                Intent intent = OrderDetailsActivity.newInstance(activity, model);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id, tv_order_time, tv_total_item, tv_cancel_order, tv_confirm_order;
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
