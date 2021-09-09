package com.sa.grabgo.vendor.orders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView;
import com.google.android.material.tabs.TabLayout;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.OrderMainAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;


public class FragmentOrderMain extends Fragment {
    public static final String TAG = "FragmentOrderMain";

    Context context;
    Activity activity;
    View mainView;

    private RelativeLayout rl_favorite_main, rl_setting_main;

    private boolean loading = true;

    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID, titleResourseID;
    static Menu menu;
    static TextView toolbar_title;
    public static TextView tv_est_time;
    static ImageView toolbar_logo;
    TabLayout tabLayout;
    ViewPager viewPager;
    OrderMainAdapter adapter;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order_main, container, false);
        tabLayout =view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

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

        toolbar = (Toolbar) view.findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        //toolbar.setPadding(0, globalFunctions.getStatusBarHeight(this), 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        //toolbar.setNavigationIcon(navIconDrawable);
        toolbar_title = (TextView) toolbar.findViewById(R.id.tv_add_category);

        tv_est_time=(TextView) toolbar.findViewById(R.id.tv_est_time);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.current_order)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.past_order)));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
         adapter = new OrderMainAdapter(getActivity(),getChildFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
