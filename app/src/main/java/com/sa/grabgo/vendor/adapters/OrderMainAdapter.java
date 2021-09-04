package com.sa.grabgo.vendor.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sa.grabgo.vendor.orders.CurrentOrderFragment;
import com.sa.grabgo.vendor.orders.PastOrderFragment;


public class OrderMainAdapter extends FragmentPagerAdapter {
    public static final String TAG = "OrderMainAdapter";

    Context context;
    int totalTabs;

    public OrderMainAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentOrderFragment currentOrderFragment = new CurrentOrderFragment();
                return currentOrderFragment;
            case 1:
                PastOrderFragment pastOrderFragment = new PastOrderFragment();
                return pastOrderFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
