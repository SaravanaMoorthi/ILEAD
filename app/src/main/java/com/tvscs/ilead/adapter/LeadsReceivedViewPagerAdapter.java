package com.tvscs.ilead.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tvscs.ilead.fragment.LeadsReceivedFragment;
import com.tvscs.ilead.fragment.PickLeadFragment;

public class LeadsReceivedViewPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_OF_FRAGMENT = 2;

    public LeadsReceivedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PickLeadFragment();
            case 1:
                return new LeadsReceivedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENT;
    }
}
