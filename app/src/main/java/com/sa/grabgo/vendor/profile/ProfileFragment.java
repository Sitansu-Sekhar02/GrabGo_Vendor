package com.sa.grabgo.vendor.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView;
import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.SplashActivity;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.sa.grabgo.vendor.services.ServerResponseInterface;
import com.sa.grabgo.vendor.services.ServicesMethodsManager;
import com.sa.grabgo.vendor.services.model.ProfileMainModel;
import com.sa.grabgo.vendor.services.model.ProfileModel;
import com.sa.grabgo.vendor.services.model.StatusModel;
import com.sa.grabgo.vendor.services.model.UpdateLanguageModel;
import com.sa.grabgo.vendor.view.AlertDialog;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sa.grabgo.vendor.MainActivity.mainContext;

public class ProfileFragment extends Fragment {

    public static String TAG = "ProfileFragment";
    public static final String BUNDLE_MAIN_NOTIFICATION_MODEL = "BundleMainModelNotificationModel";
    Context context;
    Activity activity;
    View mainView;

    private TextView tv_edit_profile, tv_view_allOrders;
    private TextView tv_restaurantName, tv_restaurant_name, tv_address, tv_restaurant_id, tv_contact, tv_logout;
    private RelativeLayout rl_favorite_main, rl_setting_main;
    // private CircleImageView iv_profile, iv_restaurant;
    NameInitialsCircleImageView iv_profile;



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
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        return view;

    }

    private void logout() {
        final AlertDialog alertDialog = new AlertDialog(activity);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setTitle(activity.getString(R.string.app_name));
        alertDialog.setMessage(activity.getResources().getString(R.string.appLogoutText));
        alertDialog.setPositiveButton(activity.getString(R.string.yes), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //Logout the Application
              /*  LogoutModel logoutModel=new LogoutModel();
                logoutModel.setUuid(GlobalFunctions.getUniqueID(activity));
                logoutUser( mainContext ,logoutModel);*/

                UpdateLanguageModel updateLanguageModel = new UpdateLanguageModel();
                if (GlobalFunctions.isNotNullValue(GlobalFunctions.getSharedPreferenceString(mainContext, GlobalVariables.SHARED_PREFERENCE_TOKEN))) {
                    updateLanguageModel.setPushToken(GlobalFunctions.getSharedPreferenceString(mainContext, GlobalVariables.SHARED_PREFERENCE_TOKEN));
                    logoutUser(mainContext, updateLanguageModel);
                }
            }
        });

        alertDialog.setNegativeButton(activity.getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void logoutUser(final Context mainContext, final UpdateLanguageModel updateLanguageModel) {
        GlobalFunctions.showProgress(mainContext, getString(R.string.logingout));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.logout(mainContext, updateLanguageModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                Log.d(TAG, "Response : " + arg0.toString());
                validateOutput(arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(mainContext, msg, Toast.LENGTH_SHORT).show();
                //GlobalFunctions.displayMessaage(context, mainView, msg);
                Log.d(TAG, "Failure : " + msg);
            }

            @Override
            public void OnError(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(mainContext, msg, Toast.LENGTH_SHORT).show();
                //GlobalFunctions.displayMessaage(context, mainView, msg);
                Log.d(TAG, "Error : " + msg);
            }
        }, "Logout_User");
    }

    private void validateOutput(Object arg0) {
        if (arg0 instanceof StatusModel) {
            StatusModel statusModel = (StatusModel) arg0;
            //globalFunctions.displayMessaage(activity, mainView, statusModel.getMessage());
            if (statusModel.isStatus()) {
                /*Logout success, Clear all cache and reload the home page*/
                globalFunctions.logoutApplication(mainContext, false);
                GlobalFunctions.closeAllActivities();
                RestartEntireApp(mainContext, false);

            }
        }

    }

    public void RestartEntireApp(Context context, boolean isLanguageChange) {
        if (isLanguageChange) {
            SharedPreferences shared_preference = PreferenceManager.getDefaultSharedPreferences(this
                    .getActivity());

            String mCustomerLanguage = shared_preference.getString(
                    globalVariables.SHARED_PREFERENCE_SELECTED_LANGUAGE, "null");
            String mCurrentlanguage;
            if ((mCustomerLanguage.equalsIgnoreCase("en"))) {
                globalFunctions.setLanguage(context, GlobalVariables.LANGUAGE.ARABIC);

                mCurrentlanguage = "ar";
            } else {
                mCurrentlanguage = "en";
                globalFunctions.setLanguage(context, GlobalVariables.LANGUAGE.ENGLISH);

            }
            SharedPreferences.Editor editor = shared_preference.edit();
            editor.putString(globalVariables.SHARED_PREFERENCE_SELECTED_LANGUAGE, mCurrentlanguage);
            editor.commit();
        }
        globalFunctions.closeAllActivities();
        Intent i = new Intent(activity, SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        System.exit(0);
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
