package com.sa.grabgo.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.home.HomeFragment;
import com.sa.grabgo.vendor.menu.MenuListFragment;
import com.sa.grabgo.vendor.orders.FragmentOrderMain;
import com.sa.grabgo.vendor.profile.ProfileFragment;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.NotificationModel;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.sa.grabgo.vendor.services.model.StatusMainModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.view.AlertDialog;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    EasyWayLocation easyWayLocation;
    GetLocationDetail getLocationDetail;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    GoogleApiClient mGoogleApiClient;

    static Intent locationintent;


    public static Context mainContext = null;

    public static final String BUNDLE_MAIN_NOTIFICATION_MODEL = "BundleMainModelNotificationModel";
    public static final String BUNDLE_KEY_TO_PAGE = "BundleKeyToPage";

    static Activity activity = null;
    Window mainWindow = null;
    FragmentManager mainActivityFM = null;

    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;

    View mainView;

    private LayoutInflater layoutInflater;

    static Toolbar toolbar;
    static ActionBar actionBar;
    static String mTitle;
    static int mResourceID, titleResourseID;
    static Menu menu;
    public static TextView toolbar_title;
    public  static TextView tv_restaurant_name;
    public static ImageView toolbar_logo,ic_logout;
    public static TextView header_tv;
    public static String address;
    Double latitude, longitude;
    int isAvailable=1;


    private NotificationModel notificationModel = null;


    static BottomNavigationView bottom_nav_view;

    public static Intent newInstance(Context mainContext, NotificationModel notificationModel) {
        Intent intent = new Intent(mainContext, MainActivity.class);
        intent.putExtra(BUNDLE_MAIN_NOTIFICATION_MODEL, notificationModel);
        return intent;
    }

    public static Intent newInstance(Context context, String toPage) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_TO_PAGE, toPage);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        mainContext = this;
        mainWindow = getWindow();
        mainActivityFM = getSupportFragmentManager();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();
        this.layoutInflater = activity.getLayoutInflater();


        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        tv_restaurant_name = (TextView) toolbar.findViewById(R.id.tv_restaurant_name); // Attaching the layout to the toolbar object
        //toolbar.setPadding(0, globalFunctions.getStatusBarHeight(this), 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        //toolbar.setNavigationIcon(navIconDrawable);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_logo = (ImageView) toolbar.findViewById(R.id.tool_bar_logo);
        ic_logout = (ImageView) toolbar.findViewById(R.id.ic_logout);


        header_tv = (TextView) findViewById(R.id.toolbar_title);

        mainView = toolbar;

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //  actionBar.setHomeAsUpIndicator(navIconDrawable);
        //   setOptionsMenuVisiblity(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorStatusBar));
        }

        bottom_nav_view = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        // navigationView.setNavigationItemSelectedListener(this);
        // navigationHeaderView = navigationView.getHeaderView(0);

        mainActivityFM.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                if (mainActivityFM != null) {
                    Fragment currentFragment = mainActivityFM.findFragmentById(R.id.container);
                    if (currentFragment != null) {
                        currentFragment.onResume();
                    }
                }
            }
        });

        getProfile();


        ic_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAvailable == 1) {
                    isAvailable = 2 ; // change the status to 2 so the at the second click , the else will be executed
                } else {
                    isAvailable = 1;//change the status to 0 so the at the second clic , the if will be executed
                }

                getAvailableStatus(isAvailable);

            }
        });


        bottom_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        Fragment homeFragment = new HomeFragment();
                        replaceFragment(homeFragment, HomeFragment.TAG, "", 0, 0);
                        break;

                    case R.id.nav_orders:

                        Fragment orderFragment = new FragmentOrderMain();
                        replaceFragment(orderFragment, FragmentOrderMain.TAG, "", 0, 0);
                        break;

                    case R.id.nav_menu:
                        Fragment menuFragment = new MenuListFragment();
                        replaceFragment(menuFragment, MenuListFragment.TAG, "", 0, 0);
                        break;

                    case R.id.nav_profile:
                        Fragment profileFragment = new ProfileFragment();
                        replaceFragment(profileFragment, ProfileFragment.TAG, "", 0, 0);
                        break;

                }

                return true;
            }
        });

        bottom_nav_view.setSelectedItemId(R.id.nav_dashboard);

        Fragment homeFragment = new HomeFragment();
        replaceFragment(homeFragment, HomeFragment.TAG, getString(R.string.app_name), 0, 0);
    }

    private void getProfile() {
        // globalFunctions.showProgress( context, getString( R.string.getting_profile ));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getProfile(mainContext, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //globalFunctions.hideProgress();
                Log.d(TAG, "Response : " + arg0.toString());
                if (arg0 instanceof ProfileMainModel) {
                    ProfileMainModel profileMainModel = (ProfileMainModel) arg0;
                    ProfileModel profileModel = profileMainModel.getProfileModel();
                    GlobalFunctions.setProfile(mainContext, profileModel);
                    setProfile();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                // globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(mainContext, mainView, msg);
                Log.d(TAG, "Failure : " + msg);
            }

            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage(mainContext, mainView, msg);
                Log.d(TAG, "Error : " + msg);
            }
        }, "Get Profile");

    }

    private void setProfile() {
        ProfileModel profileModel = globalFunctions.getProfile(mainContext);
        if (profileModel != null && mainContext != null) {
            try {

                if (GlobalFunctions.isNotNullValue(profileModel.getFullname())) {
                    MainActivity.tv_restaurant_name.setText(profileModel.getFullname());
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getAddress())) {
                    MainActivity.toolbar_title.setText(profileModel.getAddress());
                }

                if (GlobalFunctions.isNotNullValue(profileModel.getIs_available())) {
                    if (profileModel.getIs_available().equalsIgnoreCase("1")){
                        ic_logout.setImageResource(R.drawable.ic_logout_green);

                    }else{
                        ic_logout.setImageResource(R.drawable.ic_logout_red);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void getAvailableStatus(int isAvailable) {
        // globalFunctions.showProgress(activity, getString(R.string.loading));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.updateAvailableStatus(mainContext, String.valueOf(isAvailable), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // globalFunctions.hideProgress();
                Log.d(TAG, "Response: " + arg0.toString());
                StatusMainModel statusMainModel=(StatusMainModel)arg0;
                StatusModel statusModel=statusMainModel.getStatusModel();
                if (statusMainModel.isStatusLogin()) {
                    //globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());
                    getProfile();

                } else {
                    globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());

                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                // globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);
                Log.d(TAG, "Failure : " + msg);

            }

            @Override
            public void OnError(String msg) {
                //globalFunctions.hideProgress();
                globalFunctions.displayMessaage(activity, mainView, msg);

                Log.d(TAG, "Error : " + msg);
            }

        }, "MainActivity");
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
        // restoreToolbar();
    }

    private static void restoreToolbar() {
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Log.d(TAG, "Restore Tool Bar");
        if (actionBar != null) {
            Log.d(TAG, "Restore Action Bar not Null");
            Log.d(TAG, "Title : " + mTitle);
            if (titleResourseID != 0) {
                toolbar_logo.setVisibility(View.VISIBLE);
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setImageResource(titleResourseID);
            } else {
                toolbar_logo.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(mTitle);
            }
            if (mResourceID != 0) toolbar.setBackgroundResource(mResourceID);
            //actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static int getTitleResourseID() {
        return titleResourseID;
    }

    public static void setTitleResourseID(int titleResourseID) {
        MainActivity.titleResourseID = titleResourseID;
        //restoreToolbar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        //  setTitle("", 0, 0);
        //setNavigationHeaders();
        // setCartCount(globalFunctions.getCartCount());
        super.onResume();
    }

    public void setOptionsMenuVisiblity(boolean showMenu) {
        if (menu == null)
            return;
        //menu.setGroupVisible(R.id.menu, showMenu);
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mainActivityFM != null) {
            String currentFragment = mainActivityFM.findFragmentById(R.id.container).getClass().getName();
            String homeFragment = null;
            homeFragment = HomeFragment.class.getName();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            /*if (drawer.isDrawerOpen(gravity)) {
                drawer.closeDrawers();
            }*/
            if (!currentFragment.equalsIgnoreCase(homeFragment) && mainActivityFM.getBackStackEntryCount() == 0) {
                Fragment shopFragment = null;
                shopFragment = new HomeFragment();
                replaceFragment(shopFragment, HomeFragment.TAG, "", 0, 0);
//                setIcon(activity.getResources().getDrawable(R.drawable.ic_civil));
            } else if (currentFragment.equalsIgnoreCase(homeFragment)) {
                /*Exit Alert Box*/
                final AlertDialog alertDialog = new AlertDialog(mainContext);
                alertDialog.setCancelable(false);
                alertDialog.setIcon(R.drawable.app_icon);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage(getResources().getString(R.string.appExitText));
                alertDialog.setPositiveButton(getString(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        /*analyticsReport.closeApp(globalFunctions.getProfile());*/
                        finishAffinity();
                        System.exit(0);
                    }
                });

                alertDialog.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            } else {
                super.onBackPressed();
                stimulateOnResumeFunction();
            }
        } else {
            super.onBackPressed();

        }
        stimulateOnResumeFunction();

    }

    private void replaceFragment(@Nullable Fragment allFragment, @Nullable String tag, @Nullable String title, int titleImageID, @Nullable int bgResID) {
        if (allFragment != null && mainActivityFM != null) {
            Fragment tempFrag = mainActivityFM.findFragmentByTag(tag);
            if (tempFrag != null) {
//                mainActivityFM.beginTransaction().remove(tempFrag).commitAllowingStateLoss();
                mainActivityFM.beginTransaction().remove(tempFrag).commit();
            }
            // setTitle( title, titleImageID, bgResID );
            FragmentTransaction ft = mainActivityFM.beginTransaction();
            // ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            //ft.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right);
            ft.add(R.id.container, allFragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        }
    }


    public void stimulateOnResumeFunction() {
        mainActivityFM.findFragmentById(R.id.container).onResume();
    }


    public void releseFragments() {
        for (int i = 0; i < mainActivityFM.getBackStackEntryCount(); ++i) {
            mainActivityFM.popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}