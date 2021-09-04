package com.sa.grabgo.vendor.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    Activity activity;
    Context context;

    View mainView;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    static Intent locationintent;
    private boolean mAlreadyStartedService = false;

    private TextView view_all_category_tv, view_all_sub_category_tv;
    CardView search_card_view;

    private TextView tv_feedback_title, tv_preparation_time, tv_order_no, tv_chat_with_restro;
    private EditText et_feedback_comment;
    private CircleImageView iv_product_image, iv_menu_item;
    private ImageView iv_call, iv_location_to;
    private Button btn_submit;


    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;

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


        return view;
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

}
