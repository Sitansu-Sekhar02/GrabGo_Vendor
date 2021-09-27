package com.sauthi.grabgo.vendor.orders;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.sauthi.grabgo.vendor.AppController;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.adapters.PastOrderAdapter;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.services.ServerResponseInterface;
import com.sauthi.grabgo.vendor.services.ServicesMethodsManager;
import com.sauthi.grabgo.vendor.services.model.HomePageMainModel;
import com.sauthi.grabgo.vendor.services.model.HomePageModel;
import com.sauthi.grabgo.vendor.services.model.OrderListModel;
import com.sauthi.grabgo.vendor.services.model.OrderModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PastOrderFragment extends Fragment {

    public static final String TAG = "PastOrderFragment";

    Activity activity;
    Context context;
    ProgressLinearLayout progressActivity;
    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;
    View mainView;

    String status = "3";

    // current orders
    PastOrderAdapter pastOrderAdapter;
    List<OrderModel> orderModels = new ArrayList<>();
    LinearLayoutManager homeMain_linear;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView rr_past_orders;

    ProgressDialog progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_past_orders, container, false);

        activity = getActivity();
        context = getActivity();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();

        details_progressActivity = view.findViewById(R.id.details_progressActivity);
        swipe_container = view.findViewById(R.id.swipe_container);
        rr_past_orders = view.findViewById(R.id.rr_past_orders);

        homeMain_linear = new LinearLayoutManager(activity);
        mainView = rr_past_orders;

        getPastOrders();


        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPastOrders();
            }
        });

        return view;
    }

    private void getPastOrders() {
        //globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeDetails(context, status, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               // globalFunctions.hideProgress();
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
                //globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
               // globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "Past Orders");
    }

    private void setCurrentOrderList(OrderListModel orderListModel) {
        if (orderListModel != null && orderModels != null) {
            orderModels.clear();
            orderModels.addAll(orderListModel.getOrderModels());

            if (pastOrderAdapter != null) {
                synchronized (pastOrderAdapter) {
                    pastOrderAdapter.notifyDataSetChanged();
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

        rr_past_orders.setLayoutManager(homeMain_linear);
        rr_past_orders.setHasFixedSize(true);
        pastOrderAdapter = new PastOrderAdapter(activity, orderModels);
        rr_past_orders.setAdapter(pastOrderAdapter);
    }

    private void showCategoryEmptyPage() {
        if (details_progressActivity != null) {
            details_progressActivity.showEmpty(getResources().getDrawable(R.drawable.app_icon), getString(R.string.emptyList),
                    getString(R.string.no_orders));
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

}
