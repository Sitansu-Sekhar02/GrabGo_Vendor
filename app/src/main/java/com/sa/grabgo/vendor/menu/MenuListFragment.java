package com.sa.grabgo.vendor.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;

public class MenuListFragment extends Fragment {

    public static String TAG = "MenuFragment";
    Context context;
    Activity activity;
    View mainView;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID, titleResourseID;
    static Menu menu;
    static TextView toolbar_title;
    static ImageView toolbar_logo;
    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    private TextView tv_edit_profile, tv_view_allOrders;
    private TextView tv_logout, tv_user_name, etv_mobile_no, etv_country_code, tv_order_date, tv_item_title, tv_ratings, tv_rating_count, tv_distance;
    private RelativeLayout rl_favorite_main, rl_setting_main;
    // private CircleImageView iv_profile, iv_restaurant;
    NameInitialsCircleImageView iv_profile;
    private EditText etv_Comment;
    private Button btn_submit;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_list, container, false);

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
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCategoryDialog();
            }
        });

        return view;

    }

    private void openAddCategoryDialog() {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_category_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        etv_Comment=dialog.findViewById(R.id.etv_Comment);
        btn_submit=dialog.findViewById(R.id.btn_submit);
        etv_Comment.clearFocus();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dialog.dismiss();


            }

        });
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
