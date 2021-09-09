package com.sa.grabgo.vendor.orders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.CurrentOrderAdapter;
import com.sa.grabgo.vendor.adapters.HomeAdapter;
import com.sa.grabgo.vendor.adapters.interfaces.UpdateCurrentStatusInvoke;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.HomePageMainModel;
import com.sa.grabgo.vendor.services.model.HomePageModel;
import com.sa.grabgo.vendor.services.model.OrderListModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderFragment extends Fragment implements UpdateCurrentStatusInvoke {

    public static final String TAG = "CurrentOrderFragment";

    Activity activity;
    Context context;
    ProgressLinearLayout progressActivity;
    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;
    View mainView;

    String status = "2";

    // current orders
    CurrentOrderAdapter currentOrderAdapter;
    List<OrderModel> orderModels = new ArrayList<>();
    LinearLayoutManager homeMain_linear;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView rr_current_orders;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_current_orders, container, false);

        activity = getActivity();
        context = getActivity();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();

        details_progressActivity = view.findViewById(R.id.details_progressActivity);
        swipe_container = view.findViewById(R.id.swipe_container);
        rr_current_orders = view.findViewById(R.id.rr_current_orders);

        homeMain_linear = new LinearLayoutManager(activity);
        mainView = rr_current_orders;

        getCurrentOrders();


        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentOrders();
            }
        });

        return view;
    }

    private void getCurrentOrders() {
        globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeDetails(context, status, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                Log.d(TAG, "Response: " + arg0.toString());
                HomePageMainModel homePageMainModel = (HomePageMainModel) arg0;
                HomePageModel homePageModel = homePageMainModel.getHomePageModel();

                if (homePageModel.getOrderListModel() != null) {
                    OrderListModel orderListModel = homePageModel.getOrderListModel();
                    setCurrentOrderList(orderListModel);
                }


            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "Current Orders");
    }

    private void setCurrentOrderList(OrderListModel orderListModel) {
        if (orderListModel != null && orderModels != null) {
            orderModels.clear();
            orderModels.addAll(orderListModel.getOrderModels());

            if (currentOrderAdapter != null) {
                synchronized (currentOrderAdapter) {
                    currentOrderAdapter.notifyDataSetChanged();
                }
            }

            if (orderModels.size() <= 0) {
                showCategoryEmptyPage();
            } else {
                showContent();
                homeCategoryInitRecycler();
            }


        }
    }

    private void homeCategoryInitRecycler() {

        rr_current_orders.setLayoutManager(homeMain_linear);
        rr_current_orders.setHasFixedSize(true);
        currentOrderAdapter = new CurrentOrderAdapter(activity, orderModels,this);
        rr_current_orders.setAdapter(currentOrderAdapter);
    }

    private void showCategoryEmptyPage() {
        if (details_progressActivity != null) {
            details_progressActivity.showEmpty(getResources().getDrawable(R.drawable.app_icon), getString(R.string.emptyList),
                    getString(R.string.not_available));
        }
    }

    private void showContent() {
        if (details_progressActivity != null) {
            details_progressActivity.showContent();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getFragmentManager().findFragmentByTag(TAG) != null)
            getFragmentManager().findFragmentByTag(TAG).setRetainInstance(true);
    }

    @Override
    public void onStart() {

       /* if(hint != null) {
            hint.launchAutomaticHintForCall(activity.findViewById(R.id.action_call));
        }*/
//       globalFunctions.launchAutomaticHintForSearch(mainView, getString(R.string.search_title),  getString(R.string.search_description));
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void OnItemReady(OrderModel orderModel, String pageFrom, String order_id) {
        statusUpdate(pageFrom,order_id);

    }

    @Override
    public void OnItemDelivered(OrderModel orderModel, String pageFrom, String order_id) {
        statusUpdate(pageFrom,order_id);

    }

    private void statusUpdate(String pageFrom, String order_id) {
        //globalFunctions.showProgress(context, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getStatusUpdate(context,pageFrom,order_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // globalFunctions.hideProgress();
                Log.d(TAG, "Response Update : " + arg0.toString());
                StatusMainModel statusMainModel = (StatusMainModel) arg0;
                StatusModel statusModel = statusMainModel.getStatusModel();
                if (statusMainModel.isStatus()){
                    GlobalFunctions.displayDialog(activity,statusModel.getMessage());
                    getCurrentOrders();
                }else {
                    GlobalFunctions.displayMessaage(activity,mainView,statusModel.getMessage());
                }

            }

            @Override
            public void OnFailureFromServer(String msg) {
                // globalFunctions.hideProgress();

                Log.d(TAG, "Failure : " + msg);
                GlobalFunctions.displayMessaage(context, mainView, msg);
            }

            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                Log.d(TAG, "Error : " + msg);
                GlobalFunctions.displayMessaage(context, mainView, msg);
            }
        }, "Update Status");
    }
}
