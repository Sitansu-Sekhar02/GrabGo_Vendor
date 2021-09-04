package com.sa.grabgo.vendor.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;

public class ProfileFragment extends Fragment {

    public static String TAG = "ProfileFragment";
    public static final String BUNDLE_MAIN_NOTIFICATION_MODEL = "BundleMainModelNotificationModel";
    Context context;
    Activity activity;
    View mainView;
    private static int SPLASH_TIME_OUT = 2000;
    int count = 0;

    private TextView tv_edit_profile, tv_view_allOrders;
    private TextView tv_logout, tv_user_name, etv_mobile_no, etv_country_code, tv_order_date, tv_item_title, tv_ratings, tv_rating_count, tv_distance;
    private RelativeLayout rl_favorite_main, rl_setting_main;
    // private CircleImageView iv_profile, iv_restaurant;
    NameInitialsCircleImageView iv_profile;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_activity, container, false);

        activity = getActivity();
        context = getActivity();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.ColorStatusBar));
        }
        return view;

    }
    @Override
    public void onResume() {
        //getProfile();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // MainActivity.setTitleResourseID(0);
        // (( ProfileMainActivity ) activity).setTitle( getString( R.string.my_profile ), R.drawable.rezq_logo, 0 );
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
