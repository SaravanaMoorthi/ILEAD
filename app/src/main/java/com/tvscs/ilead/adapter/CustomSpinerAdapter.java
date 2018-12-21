package com.tvscs.ilead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tvscs.ilead.model.homechartmodel.Lstuserdtls;

import java.util.ArrayList;
import java.util.List;

import ilead.tvscs.com.ilead.R;

public class CustomSpinerAdapter extends ArrayAdapter<Lstuserdtls> {

    LayoutInflater inflater;
    List<Lstuserdtls> objects;

    public CustomSpinerAdapter(Context context, ArrayList<Lstuserdtls> objects) {
        super(context, 0,objects);
        // TODO Auto-generated constructor stub
        this.objects = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View v;
        /*if (position == 0) {
            TextView tv = new TextView(getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {*/
            v = getCustomView(position, convertView, parent);
       // }
        return v;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View mySpinner = inflater.inflate(R.layout.sinmple_text_view, parent,
                false);

        TextView spinnerTxtView = mySpinner.findViewById(android.R.id.text1);
        try {
            spinnerTxtView.setText(objects.get(position).getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mySpinner;
    }


}