package com.tvscs.ilead.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tvscs.ilead.fragment.CustomerDetailFragment;
import com.tvscs.ilead.fragment.FollowupsFragment;
import com.tvscs.ilead.utils.Constants;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs = 2;
    private String leadId;

    public ViewPagerAdapter(FragmentManager fm, String ileadNo) {
        super(fm);
        this.leadId = ileadNo;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                CustomerDetailFragment tab1 = new CustomerDetailFragment();
                bundle.putString(Constants.ileadNo, leadId);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                Bundle b = new Bundle();
                FollowupsFragment tab2 = new FollowupsFragment();
                b.putString(Constants.ileadNo, leadId);
                tab2.setArguments(b);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}






