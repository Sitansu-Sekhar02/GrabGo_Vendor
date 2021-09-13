package com.sa.grabgo.vendor.menu;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.adapters.MenuSubListAdapter;
import com.sa.grabgo.vendor.adapters.OrderDetailsAdapter;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.CategoryModel;
import com.sa.grabgo.vendor.services.model.MenuSubListModel;
import com.sa.grabgo.vendor.services.model.MenuSubMainModel;
import com.sa.grabgo.vendor.services.model.MenuSubModel;
import com.sa.grabgo.vendor.services.model.OrderDetailListModel;
import com.sa.grabgo.vendor.services.model.OrderDetailModel;
import com.sa.grabgo.vendor.services.model.OrderModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MenuSubListActivity extends AppCompatActivity {
    private static final String TAG = "MenuSubListActivity",
            BUNDLE_CATEGORY_MODEL = "BundleCategoryModel";



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
    private TextView  tv_add_item,tv_item_name;
    String order_id=null;



    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    CategoryModel categoryModel = null;
    String menu_id=null;
    String menu_title=null;


    MenuSubListAdapter menuSubListAdapter;
    List<MenuSubModel> menuSubModels = new ArrayList<>();
    LinearLayoutManager menuSub_linearLayout;
    ProgressLinearLayout details_progressActivity;
    SwipeRefreshLayout swipe_container;
    RecyclerView menu_sub_list_rr;

    public static Intent newInstance(Activity activity, CategoryModel model) {
        Intent intent = new Intent(activity, MenuSubListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_CATEGORY_MODEL, model);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sub_list);

        context = this;
        activity = this;


        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.tv_item_name);
        tool_bar_back_icon = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        tool_bar_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_add_item=toolbar.findViewById(R.id.tv_add_item);
        details_progressActivity=findViewById(R.id.details_progressActivity);
        swipe_container=findViewById(R.id.swipe_container);
        menu_sub_list_rr=findViewById(R.id.menu_sub_list_rr);

        menuSub_linearLayout=new LinearLayoutManager(activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorStatusBar));
        }

        //category model menu_id
        if (getIntent().hasExtra(BUNDLE_CATEGORY_MODEL)) {
            categoryModel = (CategoryModel) getIntent().getSerializableExtra(BUNDLE_CATEGORY_MODEL);
            menu_id=categoryModel.getMenu_id();
            menu_title=categoryModel.getName();

        } else {
            categoryModel = null;
        }


        mainView = toolbar;

        tv_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,AddItemActivity.class);
                startActivity(intent);
            }
        });

        getSubMenuList();



        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        toolbar_title.setText(menu_title);
        //setTitle(getString(R.string.order_details), 0, 0);
    }

    private void getSubMenuList() {
        globalFunctions.showProgress(context, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getMenuSubList(context,menu_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                 globalFunctions.hideProgress();
                Log.d(TAG, "Response Update : " + arg0.toString());
                MenuSubMainModel menuSubMainModel = (MenuSubMainModel) arg0;
                MenuSubListModel menuSubListModel = menuSubMainModel.getMenuSubListModel();
               if (menuSubListModel!=null){
                   getThisPage(menuSubListModel);
               }

            }

            @Override
            public void OnFailureFromServer(String msg) {
                 globalFunctions.hideProgress();

                Log.d(TAG, "Failure : " + msg);
                GlobalFunctions.displayMessaage(context, mainView, msg);
            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                Log.d(TAG, "Error : " + msg);
                GlobalFunctions.displayMessaage(context, mainView, msg);
            }
        }, "SubMenu List");

    }

    private void getThisPage(MenuSubListModel menuSubListModel) {

        if (menuSubListModel != null && menuSubModels != null) {
            menuSubModels.clear();
            menuSubModels.addAll(menuSubListModel.getMenuSubModels());

            if (menuSubListAdapter != null) {
                synchronized (menuSubListAdapter) {
                    menuSubListAdapter.notifyDataSetChanged();
                }
            }

            if (menuSubModels.size() <= 0) {
                subMenuEmptyPage();
            } else {
                showContent();
                subMenuInitRecycler();
            }


        }
    }

    private void subMenuInitRecycler() {
        menu_sub_list_rr.setLayoutManager(menuSub_linearLayout);
        menu_sub_list_rr.setHasFixedSize(true);
        menuSubListAdapter = new MenuSubListAdapter(activity, menuSubModels);
        menu_sub_list_rr.setAdapter(menuSubListAdapter);
    }

    private void showContent() {
        if (details_progressActivity != null) {
            details_progressActivity.showContent();
        }
    }

    private void subMenuEmptyPage() {
        if (details_progressActivity != null) {
            details_progressActivity.showEmpty(getResources().getDrawable(R.drawable.app_icon), getString(R.string.emptyList),
                    getString(R.string.not_available));
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
