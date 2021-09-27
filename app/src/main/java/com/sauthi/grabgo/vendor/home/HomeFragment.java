package com.sauthi.grabgo.vendor.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sauthi.grabgo.vendor.AppController;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.adapters.HomeAdapter;
import com.sauthi.grabgo.vendor.adapters.interfaces.UpdateStatusInterface;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.services.ServerResponseInterface;
import com.sauthi.grabgo.vendor.services.ServicesMethodsManager;
import com.sauthi.grabgo.vendor.services.model.HomePageMainModel;
import com.sauthi.grabgo.vendor.services.model.HomePageModel;
import com.sauthi.grabgo.vendor.services.model.MonthlyModel;
import com.sauthi.grabgo.vendor.services.model.OrderListModel;
import com.sauthi.grabgo.vendor.services.model.OrderModel;
import com.sauthi.grabgo.vendor.services.model.StatusMainModel;
import com.sauthi.grabgo.vendor.services.model.StatusModel;
import com.sauthi.grabgo.vendor.services.model.WeeklyModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UpdateStatusInterface {
    public static final String TAG = "HomeFragment";
    Activity activity;
    Context context;

    View mainView;


    private TextView tv_td_currency, tv_td_price, tv_td_orders, tv_currency, tv_revenue_price, tv_total_orders;

    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;
    String status = "1";

    //home main category
    HomeAdapter homeAdapter;
    List<OrderModel> orderModels = new ArrayList<>();
    LinearLayoutManager homeMain_linear;
    ProgressLinearLayout home_category_progress;
    SwipeRefreshLayout swipe_container;
    RecyclerView rr_home_category;

    ProgressDialog progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        activity = getActivity();
        context = getActivity();
        setHasOptionsMenu(true);


        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();


        rr_home_category = view.findViewById(R.id.rr_home_category);
        home_category_progress = view.findViewById(R.id.home_category_progress);
        tv_td_currency = view.findViewById(R.id.tv_td_currency);
        tv_td_price = view.findViewById(R.id.tv_td_price);
        tv_td_orders = view.findViewById(R.id.tv_td_orders);
        tv_currency = view.findViewById(R.id.tv_currency);
        tv_revenue_price = view.findViewById(R.id.tv_revenue_price);
        tv_total_orders = view.findViewById(R.id.tv_total_orders);
        swipe_container = view.findViewById(R.id.swipe_container);


        homeMain_linear = new LinearLayoutManager(activity);
        mainView = rr_home_category;

        progress = new ProgressDialog(activity);
        progress.setTitle(activity.getString(R.string.loading));
        progress.setMessage(activity.getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.show();

        homePageApi();


        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePageApi();
            }
        });

        return view;
    }

    private void homePageApi() {
      // globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeDetails(context, status, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // globalFunctions.hideProgress();
                progress.dismiss();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                Log.d(TAG, "Response: " + arg0.toString());
                HomePageMainModel homePageMainModel = (HomePageMainModel) arg0;
                HomePageModel homePageModel = homePageMainModel.getHomePageModel();


                if (homePageModel.getMonthlyModel() != null) {
                    MonthlyModel monthlyModel = homePageModel.getMonthlyModel();
                    setMonthlyDetails(monthlyModel);
                }

                if (homePageModel.getWeeklyModel() != null) {
                    WeeklyModel weeklyModel = homePageModel.getWeeklyModel();
                    setWeeklyDetails(weeklyModel);
                }

                if (homePageModel.getOrderListModel() != null) {
                    OrderListModel orderListModel = homePageModel.getOrderListModel();
                    setUpHomeOrderList(orderListModel);
                }


            }

            @Override
            public void OnFailureFromServer(String msg) {
                // globalFunctions.hideProgress();
                progress.dismiss();

                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
                progress.dismiss();
                //globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "Home Page");
    }

    private void setWeeklyDetails(WeeklyModel weeklyModel) {
        if (weeklyModel != null && context != null) {
            if (GlobalFunctions.isNotNullValue(weeklyModel.getTotal())) {
                tv_revenue_price.setText(weeklyModel.getTotal());

            }
            if (GlobalFunctions.isNotNullValue(weeklyModel.getTotal_order())) {
                tv_total_orders.setText(weeklyModel.getTotal_order());
            }
            if (GlobalFunctions.isNotNullValue(weeklyModel.getCurrency())) {
                tv_currency.setText(weeklyModel.getCurrency());
            }
        }

    }

    private void setMonthlyDetails(MonthlyModel monthlyModel) {
        if (monthlyModel != null && context != null) {
            if (GlobalFunctions.isNotNullValue(monthlyModel.getTotal())) {
                tv_td_price.setText(monthlyModel.getTotal());

            }
            if (GlobalFunctions.isNotNullValue(monthlyModel.getTotal_order())) {
                tv_td_orders.setText(monthlyModel.getTotal_order());
            }
            if (GlobalFunctions.isNotNullValue(monthlyModel.getCurrency())) {
                tv_td_currency.setText(monthlyModel.getCurrency());
            }
        }
    }

    private void setUpHomeOrderList(OrderListModel orderListModel) {

        if (orderListModel != null && orderModels != null) {
            orderModels.clear();
            orderModels.addAll(orderListModel.getOrderModels());

            if (homeAdapter != null) {
                synchronized (homeAdapter) {
                    homeAdapter.notifyDataSetChanged();
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

        rr_home_category.setLayoutManager(homeMain_linear);
        rr_home_category.setHasFixedSize(true);
        homeAdapter = new HomeAdapter(activity, orderModels, this);
        rr_home_category.setAdapter(homeAdapter);
    }

    private void showCategoryEmptyPage() {
        if (home_category_progress != null) {
            home_category_progress.showEmpty(getResources().getDrawable(R.drawable.app_icon), getString(R.string.emptyList),
                    getString(R.string.no_orders));
        }
    }

    private void showContent() {
        if (home_category_progress != null) {
            home_category_progress.showContent();
        }
    }



    @Override
    public void onResume() {
//        MainActivity.setNavigationBottomIcon(TAG);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        super.onResume();
    }

    @Override
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
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void OnCancelClickListener(OrderModel orderModel, String pageFrom, String order_id) {
        statusUpdate(pageFrom, order_id);

    }

    @Override
    public void OnConfirmClickListener(OrderModel orderModel, String pageFrom, String order_id) {

        statusUpdate(pageFrom, order_id);
    }

    private void statusUpdate(String pageFrom, String order_id) {
        //globalFunctions.showProgress(context, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getStatusUpdate(context, pageFrom, order_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // globalFunctions.hideProgress();
                Log.d(TAG, "Response Update : " + arg0.toString());
                validateUpdateStatus(arg0);

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

    private void validateUpdateStatus(Object arg0) {
        if (arg0 instanceof StatusMainModel) {
            StatusMainModel statusMainModel = (StatusMainModel) arg0;
            StatusModel statusModel = statusMainModel.getStatusModel();
            globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());
            if (statusModel.isStatus()) {
                homePageApi();
            }
        }
    }
}
