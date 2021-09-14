package com.sa.grabgo.vendor.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.CategoryListAdapter;
import com.sa.grabgo.vendor.adapters.MenuListAdapter;
import com.sa.grabgo.vendor.adapters.MenuSubListAdapter;
import com.sa.grabgo.vendor.adapters.MenuTypeListAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.CategoryListModel;
import com.sa.grabgo.vendor.services.model.CategoryMainModel;
import com.sa.grabgo.vendor.services.model.CategoryModel;
import com.sa.grabgo.vendor.services.model.MenuSubModel;
import com.sa.grabgo.vendor.services.model.MenuTypeListModel;
import com.sa.grabgo.vendor.services.model.MenuTypeMainModel;
import com.sa.grabgo.vendor.services.model.MenuTypeModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemListActivity extends AppCompatActivity   {
    private static final String TAG = "CategoryItemListActivity";
   public static final String BUNDLE_SEARCH_RESPONSE_MODEL = "Bundle_Search_Response_Model",
           BUNDLE_SEARCH_TYPE = "BundleSearchType";



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

    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;


    CategoryListAdapter categoryListAdapter;
    List<CategoryModel> categoryModels = new ArrayList<>();
    LinearLayoutManager category_linearLayout;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView order_list_rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_list);

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

        details_progressActivity=findViewById(R.id.details_progressActivity);
        order_list_rr=findViewById(R.id.order_list_rr);
        swipe_container=findViewById(R.id.swipe_container);
        category_linearLayout=new LinearLayoutManager(activity);

        mainView = toolbar;

        getCategoryTypeList();


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setTitle(getString(R.string.select_type), 0, 0);
    }

    private void getCategoryTypeList() {
        globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getCategoryList(context, new ServerResponseInterface() {
            @SuppressLint("LongLogTag")
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();

                Log.d(TAG, "Response: " + arg0.toString());
                CategoryMainModel categoryMainModel = (CategoryMainModel) arg0;
                CategoryListModel categoryListModel = categoryMainModel.getCategoryListModel();

                if (categoryListModel.getCategoryList() != null) {
                    setThisPage(categoryListModel);
                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @SuppressLint("LongLogTag")
            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "Current Orders");
    }

    private void setThisPage(CategoryListModel categoryListModel) {
        if (categoryListModel != null && categoryModels != null) {
            categoryModels.clear();
            categoryModels.addAll(categoryListModel.getCategoryList());

            if (categoryListAdapter != null) {
                synchronized (categoryListAdapter) {
                    categoryListAdapter.notifyDataSetChanged();
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
        order_list_rr.setLayoutManager(category_linearLayout);
        order_list_rr.setHasFixedSize(true);
        categoryListAdapter = new CategoryListAdapter(activity, categoryModels);
        order_list_rr.setAdapter(categoryListAdapter);
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
