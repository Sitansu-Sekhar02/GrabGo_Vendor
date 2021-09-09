package com.sa.grabgo.vendor.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.CurrentOrderAdapter;
import com.sa.grabgo.vendor.adapters.HomeAdapter;
import com.sa.grabgo.vendor.adapters.MenuListAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.AddCategoryModel;
import com.sa.grabgo.vendor.services.model.CategoryListModel;
import com.sa.grabgo.vendor.services.model.CategoryMainModel;
import com.sa.grabgo.vendor.services.model.CategoryModel;
import com.sa.grabgo.vendor.services.model.HomePageMainModel;
import com.sa.grabgo.vendor.services.model.HomePageModel;
import com.sa.grabgo.vendor.services.model.OrderListModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

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

    AddCategoryModel addCategoryModel = null;


    MenuListAdapter menuListAdapter;
    List<CategoryModel> categoryModels = new ArrayList<>();
    LinearLayoutManager category_linearLayout;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView menu_list_rr;

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

        details_progressActivity = view.findViewById(R.id.details_progressActivity);
        swipe_container = view.findViewById(R.id.swipe_container);
        menu_list_rr = view.findViewById(R.id.menu_list_rr);

        category_linearLayout = new LinearLayoutManager(activity);
        mainView = toolbar;

        getMenuList();

        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenuList();
            }
        });

        return view;

    }

    private void getMenuList() {
        globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getCategoryList(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                if (swipe_container.isRefreshing()) {
                    swipe_container.setRefreshing(false);
                }
                Log.d(TAG, "Response: " + arg0.toString());
                CategoryMainModel categoryMainModel = (CategoryMainModel) arg0;
                CategoryListModel categoryListModel = categoryMainModel.getCategoryListModel();

                if (categoryListModel.getCategoryList() != null) {
                    setThisPage(categoryListModel);
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

    private void setThisPage(CategoryListModel categoryListModel) {
        if (categoryListModel != null && categoryModels != null) {
            categoryModels.clear();
            categoryModels.addAll(categoryListModel.getCategoryList());

            if (menuListAdapter != null) {
                synchronized (menuListAdapter) {
                    menuListAdapter.notifyDataSetChanged();
                }
            }

            if (categoryModels.size() <= 0) {
                showCategoryEmptyPage();
            } else {
                showContent();
                homeCategoryInitRecycler();
            }


        }
    }

    private void homeCategoryInitRecycler() {

        menu_list_rr.setLayoutManager(category_linearLayout);
        menu_list_rr.setHasFixedSize(true);
        menuListAdapter = new MenuListAdapter(activity, categoryModels);
        menu_list_rr.setAdapter(menuListAdapter);
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

        etv_Comment = dialog.findViewById(R.id.etv_Comment);
        btn_submit = dialog.findViewById(R.id.btn_submit);
        etv_Comment.clearFocus();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String
                        category = etv_Comment.getText().toString().trim();

                if (addCategoryModel == null) {
                    addCategoryModel = new AddCategoryModel();
                }
                addCategoryModel.setName(category);

                insertCategort(activity, addCategoryModel);
                dialog.dismiss();


            }

        });
    }

    private void insertCategort(Activity activity, AddCategoryModel addCategoryModel) {
        globalFunctions.showProgress(activity, activity.getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.insertCategory(context, addCategoryModel, new ServerResponseInterface() {
            @SuppressLint("LongLogTag")
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                Log.d(TAG, "Response : " + arg0.toString());
                validateOutputAfterFeedback(arg0);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failure : " + msg);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error : " + msg);
            }
        }, "Add Category");
    }

    private void validateOutputAfterFeedback(Object arg0) {
        if (arg0 instanceof StatusMainModel) {
            StatusMainModel statusMainModel = (StatusMainModel) arg0;
            StatusModel statusModel = statusMainModel.getStatusModel();
            if (!statusMainModel.isStatusLogin()) {
                globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());

            } else {
                globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());
                getMenuList();

            }
        }
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
