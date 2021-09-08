package com.sa.grabgo.vendor.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public static String TAG = "ProfileFragment";
    public static final String BUNDLE_MAIN_NOTIFICATION_MODEL = "BundleMainModelNotificationModel";
    Context context;
    Activity activity;
    View mainView;
    private static int SPLASH_TIME_OUT = 2000;
    int count = 0;

    private TextView tv_edit_profile, tv_view_allOrders;
    private TextView tv_restaurantName, tv_restaurant_name, tv_address, tv_restaurant_id, tv_contact, tv_logout;
    private RelativeLayout rl_favorite_main, rl_setting_main;
    // private CircleImageView iv_profile, iv_restaurant;
    NameInitialsCircleImageView iv_profile;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    GlobalVariables globalVariables;
    GlobalFunctions globalFunctions;

    ProfileModel profileModel = null;

    public ProfileFragment() {
        setHasOptionsMenu( true );
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu( true );
        super.onCreate( savedInstanceState );
    }

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

        tv_restaurantName = view.findViewById( R.id.tv_restaurantName );
        tv_restaurant_name =  view.findViewById( R.id.tv_restaurant_name );
        tv_address =  view.findViewById( R.id.tv_address );
        tv_restaurant_id = view.findViewById( R.id.tv_restaurant_id);
        tv_contact = view.findViewById( R.id.tv_contact );
        tv_logout = view.findViewById( R.id.tv_logout );
        iv_profile = view.findViewById( R.id.iv_profile );
        mainView=tv_restaurantName;

        getProfile();


        return view;

    }

    private void getProfile() {
        globalFunctions.showProgress( context, getString( R.string.getting_profile ));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getProfile( context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               globalFunctions.hideProgress();
                Log.d( TAG, "Response : " + arg0.toString() );
                if (arg0 instanceof ProfileMainModel) {
                    ProfileMainModel profileMainModel = (ProfileMainModel) arg0;
                    ProfileModel profileModel = profileMainModel.getProfileModel();
                    GlobalFunctions.setProfile(context, profileModel);
                    setProfile();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage( context, mainView, msg );
                Log.d( TAG, "Failure : " + msg );
            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                GlobalFunctions.displayMessaage( context, mainView, msg );
                Log.d( TAG, "Error : " + msg );
            }
        }, "Get Profile" );

    }

    private void setProfile() {
        ProfileModel profileModel = globalFunctions.getProfile(context);
        if (profileModel != null && context != null) {
            try {

                // name_tv.setText( fullName );
                if (GlobalFunctions.isNotNullValue(profileModel.getFullname())){
                    tv_restaurantName.setText( profileModel.getFullname() );
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getFullname())){
                    tv_restaurant_name.setText(  profileModel.getFullname());
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getEmail())){
                    tv_restaurant_id.setText(  profileModel.getEmail() );
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getAddress())){
                    tv_address.setText(  profileModel.getAddress() );
                }
                if (GlobalFunctions.isNotNullValue(profileModel.getFullname()) && GlobalFunctions.isNotNullValue(profileModel.getCountry_code())){
                    tv_contact.setText( profileModel.getCountry_code()+" "+profileModel.getPhone() );
                }


                try {
                    if (profileModel.getImage() != null || !profileModel.getImage().equals("null") || !profileModel.getImage().equalsIgnoreCase("")) {

                        String firstLetter = String.valueOf(profileModel.getFullname().charAt(0));
                        NameInitialsCircleImageView.ImageInfo imageInfo = new NameInitialsCircleImageView.ImageInfo
                                .Builder(firstLetter)
                                .setTextColor(android.R.color.primary_text_dark)
                                .setImageUrl(profileModel.getImage())
                                .setCircleBackgroundColorRes(android.R.color.black)
                                .build();
                        iv_profile.setImageInfo(imageInfo);

                        // Picasso.with(mainContext).load(profileModel.getProfileImg()).placeholder(R.drawable.lazy_load).into(iv_profile);
                    }
                } catch (Exception e) {
                }



            } catch (Exception exxx) {
                Log.e(TAG, exxx.getMessage());
            }

        }
    }

    @Override
    public void onResume() {
//        getProfile();
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
