package com.sa.grabgo.vendor.menu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.HomeAdapter;
import com.sa.grabgo.vendor.adapters.MenuTypeListAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.image_picker.ImagePickerActivity;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.MenuModel;
import com.sa.grabgo.vendor.services.model.MenuTypeListModel;
import com.sa.grabgo.vendor.services.model.MenuTypeMainModel;
import com.sa.grabgo.vendor.services.model.MenuTypeModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.upload.UploadImage;
import com.sa.grabgo.vendor.upload.UploadListener;
import com.sa.grabgo.vendor.view.AlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemListActivity extends AppCompatActivity   {
    private static final String TAG = "MenuItemListActivity";
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



    MenuTypeListAdapter menuTypeListAdapter;
    List<MenuTypeModel> menuTypeModel = new ArrayList<>();
    LinearLayoutManager category_linearLayout;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView order_list_rr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_list);

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

        getMenuTypeList();


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setTitle(getString(R.string.select_type), 0, 0);
    }

    private void getMenuTypeList() {
        globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getItemList(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                globalFunctions.hideProgress();
                Log.d(TAG, "Response: " + arg0.toString());
                MenuTypeMainModel menuTypeMainModel = (MenuTypeMainModel) arg0;
                MenuTypeListModel menuTypeListModel = menuTypeMainModel.getMenuTypeListModel();

                if (menuTypeListModel.getMenuTypeModels() != null) {
                    setThisPage(menuTypeListModel);
                }

            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "MenuType List");
    }

    private void setThisPage(MenuTypeListModel menuTypeListModel) {
        if (menuTypeListModel != null && menuTypeModel != null) {
            menuTypeModel.clear();
            menuTypeModel.addAll(menuTypeListModel.getMenuTypeModels());

            if (menuTypeListAdapter != null) {
                synchronized (menuTypeListAdapter) {
                    menuTypeListAdapter.notifyDataSetChanged();
                }
            }

            if (menuTypeModel.size() <= 0) {
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
        menuTypeListAdapter = new MenuTypeListAdapter(activity, menuTypeModel);
        order_list_rr.setAdapter(menuTypeListAdapter);
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
