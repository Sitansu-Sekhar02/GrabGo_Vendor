package com.sauthi.grabgo.vendor.menu;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sauthi.grabgo.vendor.AppController;
import com.sauthi.grabgo.vendor.R;
import com.sauthi.grabgo.vendor.adapters.MenuTypeListAdapter;
import com.sauthi.grabgo.vendor.adapters.interfaces.MenuTypeItemClick;
import com.sauthi.grabgo.vendor.global.GlobalFunctions;
import com.sauthi.grabgo.vendor.global.GlobalVariables;
import com.sauthi.grabgo.vendor.services.model.MenuTypeListModel;
import com.sauthi.grabgo.vendor.services.model.MenuTypeModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MenuItemListActivity extends AppCompatActivity implements MenuTypeItemClick {

    private static final String TAG = "MenuItemListActivity";

   public static final String
           BUNDLE_MENU_RESPONSE_MODEL = "Bundle_Menu_Response_Model",
           BUNDLE_SEARCH_TYPE = "BundleSearchType";



    Context context;
    private static Activity activity;
    View mainView;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID;
    static int titleResourseID;
    static TextView toolbar_title,tv_cancel,tv_submit;
    static ImageView toolbar_logo, tool_bar_back_icon;


    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    MenuTypeListAdapter menuTypeListAdapter;
    List<MenuTypeModel> menuTypeModel = new ArrayList<>();
    LinearLayoutManager category_linearLayout;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView order_list_rr;

    MenuTypeListModel menuTypeListModel=null;
    MenuTypeModel selectedCategoryModel =null;


    public static Intent newInstance(Activity activity, MenuTypeListModel menuModel) {
        Intent intent = new Intent(activity, MenuItemListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_MENU_RESPONSE_MODEL, menuModel);
        intent.putExtras(bundle);
        return intent;
    }

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

        tv_cancel=findViewById(R.id.tv_cancel);
        tv_submit=findViewById(R.id.tv_submit);
        details_progressActivity=findViewById(R.id.details_progressActivity);
        order_list_rr=findViewById(R.id.order_list_rr);
        category_linearLayout=new LinearLayoutManager(activity);

        mainView = toolbar;

        if (getIntent().hasExtra(BUNDLE_MENU_RESPONSE_MODEL)) {

            menuTypeListModel = (MenuTypeListModel) getIntent().getSerializableExtra(BUNDLE_MENU_RESPONSE_MODEL);
        } else {
            menuTypeListModel = null;
        }


        setThisPage(menuTypeListModel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeThisActivity();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTypeResult(true, selectedCategoryModel);

            }
        });

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setTitle(getString(R.string.select_type), 0, 0);
    }

    private void setTypeResult(boolean isSuccess, MenuTypeModel model) {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_MENU_RESPONSE_MODEL, model);
        if (isSuccess) setResult(RESULT_OK, intent);
        else setResult(RESULT_CANCELED, intent);
        closeThisActivity();
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
        menuTypeListAdapter = new MenuTypeListAdapter(activity, menuTypeModel,this);
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

    @Override
    public void OnMenuItemClickListener(MenuTypeModel menuTypeModel) {
        selectedCategoryModel=menuTypeModel;

    }
}
