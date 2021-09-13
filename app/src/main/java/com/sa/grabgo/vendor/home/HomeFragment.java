package com.sa.grabgo.vendor.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.MainActivity;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.HomeAdapter;
import com.sa.grabgo.vendor.adapters.interfaces.UpdateStatusInterface;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.HomePageMainModel;
import com.sa.grabgo.vendor.services.model.HomePageModel;
import com.sa.grabgo.vendor.services.model.MonthlyModel;
import com.sa.grabgo.vendor.services.model.OrderListModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.services.model.WeeklyModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

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
        getProfile();

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
        homeAdapter = new HomeAdapter(activity, orderModels,this);
        rr_home_category.setAdapter(homeAdapter);
    }

    private void showCategoryEmptyPage() {
        if (home_category_progress != null) {
            home_category_progress.showEmpty(getResources().getDrawable(R.drawable.app_icon), getString(R.string.emptyList),
                    getString(R.string.not_available));
        }
    }

    private void showContent() {
        if (home_category_progress != null) {
            home_category_progress.showContent();
        }
    }

    private void getProfile() {
        // globalFunctions.showProgress( context, getString( R.string.getting_profile ));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getProfile(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //globalFunctions.hideProgress();
                Log.d(TAG, "Response : " + arg0.toString());
                if (arg0 instanceof ProfileMainModel) {
                    ProfileMainModel profileMainModel = (ProfileMainModel) arg0;
                    ProfileModel profileModel = profileMainModel.getProfileModel();
                    GlobalFunctions.setProfile(context, profileModel);
                    setProfile();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                // globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(context, mainView, msg);
                Log.d(TAG, "Failure : " + msg);
            }

            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(context, mainView, msg);
                Log.d(TAG, "Error : " + msg);
            }
        }, "Get Profile");

    }

    private void setProfile() {
        ProfileModel profileModel = globalFunctions.getProfile(context);
        if (profileModel != null && context != null) {
            try {

                if (GlobalFunctions.isNotNullValue(profileModel.getFullname())) {
                    MainActivity.tv_restaurant_name.setText(profileModel.getFullname());
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getAddress())) {
                    MainActivity.toolbar_title.setText(profileModel.getAddress());
                }

                if (GlobalFunctions.isNotNullValue(profileModel.getIs_available())) {
                    MainActivity.ic_logout.setImageResource(R.drawable.ic_logout_green);
                }else {
                    MainActivity.ic_logout.setImageResource(R.drawable.ic_logout_red);

                }

            } catch (Exception exxx) {
                Log.e(TAG, exxx.getMessage());
            }

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
        statusUpdate(pageFrom,order_id);

    }

    @Override
    public void OnConfirmClickListener(OrderModel orderModel, String pageFrom, String order_id) {

        statusUpdate(pageFrom, order_id);
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
                    homePageApi();

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
