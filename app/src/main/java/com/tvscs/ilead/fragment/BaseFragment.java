package com.tvscs.ilead.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvscs.ilead.utils.Constants;

public abstract class BaseFragment extends Fragment implements Constants {

    public BaseFragment newInstance() {
        Bundle args = new Bundle();
        BaseFragment fragment = baseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        View view = fragmentView(inflater, parent, savedInstanseState);
        return view;
    }

    public abstract BaseFragment baseFragment();

    public abstract View fragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

}