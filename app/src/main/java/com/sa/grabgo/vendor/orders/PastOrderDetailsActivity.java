package com.sa.grabgo.vendor.orders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.OrderDetailsAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.model.OrderDetailListModel;
import com.sa.grabgo.vendor.services.model.OrderDetailModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PastOrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PastOrderDetailsActivity",
            BUNDLE_PAST_ORDER_DETAILS = "BundlePastOrderDetails";



    Context context;
    private static Activity activity;
    View mainView;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID;
    static int titleResourseID;
    static TextView toolbar_title;
    static ImageView toolbar_logo, tool_bar_back_icon;

    private Button btn_login;
    private TextView  tv_order_id,tv_order_time,tv_cooking_instruction,sub_total_tv,tv_packaging_price,tv_vat_price,tv_grant_total,tv_payment_type,tv_status,tv_decline,tv_confirm;
    String order_id=null;



    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    OrderModel orderModel = null;

    //Order details
    OrderDetailsAdapter orderDetailsAdapter;
    List<OrderDetailModel> orderDetailModels = new ArrayList<>();
    LinearLayoutManager orderMail_linear;
    ProgressLinearLayout home_category_progress;
    RecyclerView rv_order_details;

    public static Intent newInstance(Activity activity, OrderModel model) {
        Intent intent = new Intent(activity, PastOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PAST_ORDER_DETAILS, model);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        context = this;
        activity = this;


        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tool_bar_back_icon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        tool_bar_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorStatusBar));
        }

        //search from home
        if (getIntent().hasExtra(BUNDLE_PAST_ORDER_DETAILS)) {
            orderModel = (OrderModel) getIntent().getSerializableExtra(BUNDLE_PAST_ORDER_DETAILS);

        } else {
            orderModel = null;
        }


        tv_order_id=findViewById(R.id.tv_order_id);
        tv_order_time=findViewById(R.id.tv_order_time);
        tv_cooking_instruction=findViewById(R.id.tv_cooking_instruction);
        sub_total_tv=findViewById(R.id.sub_total_tv);
        tv_packaging_price=findViewById(R.id.tv_packaging_price);
        tv_vat_price=findViewById(R.id.tv_vat_price);
        tv_grant_total=findViewById(R.id.tv_grant_total);
        tv_payment_type=findViewById(R.id.tv_payment_type);
        tv_decline=findViewById(R.id.tv_decline);
        tv_confirm=findViewById(R.id.tv_confirm);
        rv_order_details=findViewById(R.id.rv_order_details);
        tv_status=findViewById(R.id.tv_status);
        tv_decline.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);

        orderMail_linear=new LinearLayoutManager(activity);

        mainView = toolbar;

        setOrderDetails(orderModel);
        getOrderSubDetails(orderModel.getOrder_details());


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setTitle(getString(R.string.order_details), 0, 0);
    }

    private void getOrderSubDetails(OrderDetailListModel order_details) {

        if (order_details != null && orderDetailModels != null) {
            orderDetailModels.clear();
            orderDetailModels.addAll(order_details.getOrderDetailModels());

            if (orderDetailsAdapter != null) {
                synchronized (orderDetailsAdapter) {
                    orderDetailsAdapter.notifyDataSetChanged();
                }
            }

            if (orderDetailModels.size() > 0) {
                orderDetailsInitRecyclerview();
            }

        }
    }

    private void orderDetailsInitRecyclerview() {
        rv_order_details.setLayoutManager(orderMail_linear);
        rv_order_details.setHasFixedSize(true);
        orderDetailsAdapter = new OrderDetailsAdapter(activity, orderDetailModels);
        rv_order_details.setAdapter(orderDetailsAdapter);
    }

    private void setOrderDetails(OrderModel orderModel) {

        if (orderModel != null && context != null) {

            if (GlobalFunctions.isNotNullValue(orderModel.getOrder_id())) {
                tv_order_id.setText(orderModel.getOrder_id());
                order_id=orderModel.getOrder_id();
            }

            if (GlobalFunctions.isNotNullValue(orderModel.getCreated_on())) {
                tv_order_time.setText(GlobalFunctions.getDateFormat(orderModel.getCreated_on()));
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getInstruction())) {
                tv_cooking_instruction.setText(orderModel.getInstruction());
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getSub_total())) {
                sub_total_tv.setText(orderModel.getSub_total());
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getPacking_charges())) {
                tv_packaging_price.setText(orderModel.getPacking_charges());
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getVat())) {
                tv_vat_price.setText(orderModel.getVat());
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getGrand_total())) {
                tv_grant_total.setText(orderModel.getGrand_total());
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getPayment_type())) {
                if (orderModel.getPayment_type().equalsIgnoreCase("1")){
                    tv_payment_type.setText(getString(R.string.cod));

                } else  if (orderModel.getPayment_type().equalsIgnoreCase("2")){
                    tv_payment_type.setText(getString(R.string.online));
                }else {

                }
            }
            if (GlobalFunctions.isNotNullValue(orderModel.getStatus_title())) {
                tv_status.setText(orderModel.getStatus_title());
            }

        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static void setTitle(String title, int titleImageID, int backgroundResourceID) {
        mTitle = title;
        if (backgroundResourceID != 0) {
            mResourceID = backgroundResourceID;
        } else {
            mResourceID = 0;
        }
        if (titleImageID != 0) {
            titleResourseID = titleImageID;
        } else {
            titleResourseID = 0;
        }
        restoreToolbar();
    }

    @SuppressLint("LongLogTag")
    private static void restoreToolbar() {
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Log.d(TAG, "Restore Tool Bar");
        if (actionBar != null) {
            Log.d(TAG, "Restore Action Bar not Null");
            Log.d(TAG, "Title : " + mTitle);
            toolbar_title.setText(mTitle);
            if (mResourceID != 0) toolbar.setBackgroundResource(mResourceID);
            //actionBar.setTitle("");
            // actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onBackPressed() {
        closeThisActivity();
        super.onBackPressed();
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
            //activity.overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
        }
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
        if (activity != null) activity = null;
        super.onDestroy();
    }

}
