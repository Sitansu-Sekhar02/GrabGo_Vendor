package com.sa.grabgo.vendor.orders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sa.grabgo.vendor.AppController;
import com.sa.grabgo.vendor.R;
import com.sa.grabgo.vendor.global.GlobalFunctions;
import com.sa.grabgo.vendor.global.GlobalVariables;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

public class CurrentOrderFragment extends Fragment {

    public static final String TAG = "CurrentOrderFragment";

    Activity activity;
    Context context;
    ProgressLinearLayout progressActivity;
    GlobalFunctions globalFunctions;
    GlobalVariables globalVariables;
    View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_current_orders, container, false);

        activity = getActivity();
        context = getActivity();

        globalFunctions = AppController.getInstance().getGlobalFunctions();
        globalVariables = AppController.getInstance().getGlobalVariables();

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach( context );
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getFragmentManager().findFragmentByTag( TAG ) != null)
            getFragmentManager().findFragmentByTag( TAG ).setRetainInstance( true );
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
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
