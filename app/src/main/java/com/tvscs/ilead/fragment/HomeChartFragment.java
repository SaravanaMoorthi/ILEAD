package com.tvscs.ilead.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.tvscs.ilead.helper.SharedPreference;
import com.tvscs.ilead.model.LegendModel;
import com.tvscs.ilead.model.homechartmodel.HomeChartsModel;
import com.tvscs.ilead.model.homechartmodel.RefConeResult;
import com.tvscs.ilead.retrofit.APICallInterface;
import com.tvscs.ilead.retrofit.APIClient;
import com.tvscs.ilead.utils.LoaderDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ilead.tvscs.com.ilead.R;


public class HomeChartFragment extends BaseFragment {

    protected final String[] parties = new String[]{
            "C0", "C1", "C2", "C3", "C4"
    };
    protected Integer[] colorsArray;
    protected Typeface tfRegular;
    protected Typeface tfLight;
    @BindView(R.id.txtView_homechartEmpty)
    TextView txtView_homechartEmpty;
    @BindView(R.id.homelayout_legend)
    LinearLayout homelayout_legend;
    Context mContext;
    @BindView(R.id.homechart)
    PieChart chart;
    APICallInterface mAPICallInterface;
    private ArrayList<LegendModel> legendArrayList = new ArrayList<>();
    private HomeChartsModel homeChartsModel;
    private LoaderDialog viewDialog;
    private List<RefConeResult> entryValues = new ArrayList<>();
    private SharedPreference sharedPreference;

    public static HomeChartFragment newInstance(HomeChartsModel homeChartsModel) {
        HomeChartFragment fragment = new HomeChartFragment();
        Bundle args = new Bundle();
        if (homeChartsModel != null) {
            args.putSerializable("homeChartsModel", homeChartsModel);
            fragment.setArguments(args);
        }
        return fragment;
    }


    @Override
    public BaseFragment baseFragment() {
        return new HomeChartFragment();
    }

    @Override
    public View fragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_homechart, parent, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        mContext = getActivity();
        viewDialog = new LoaderDialog(mContext);
        mAPICallInterface = APIClient.getAPIService();
        sharedPreference = new SharedPreference(mContext);
        initChartView();

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("homeChartsModel")) {
                homeChartsModel = (HomeChartsModel) args.getSerializable("homeChartsModel");
                try {
//                    getBikeLeadService(homeChartsModel);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void getBikeLeadService(HomeChartsModel homeChartsModel) {
//        try {
//
//            entryValues.addAll(homeChartsModel.getRefConeResult());
//            ArrayList<PieEntry> entryArrayList = new ArrayList<>();
//            for (int i = 0; i < entryValues.size(); i++) {
//                entryArrayList.add(new PieEntry(Float.parseFloat(entryValues.get(0).getCzero()), parties[0]));
//                entryArrayList.add(new PieEntry(Float.parseFloat(entryValues.get(0).getCone()), parties[1]));
//                entryArrayList.add(new PieEntry(Float.parseFloat(entryValues.get(0).getCtwo()), parties[2]));
//                entryArrayList.add(new PieEntry(Float.parseFloat(entryValues.get(0).getCthree()), parties[3]));
//                entryArrayList.add(new PieEntry(Float.parseFloat(entryValues.get(0).getCfour()), parties[4]));
//            }
//
//            if (Float.parseFloat(entryValues.get(0).getCzero()) == 0.0f && Float.parseFloat(entryValues.get(0).getCone()) == 0.0f &&
//                    Float.parseFloat(entryValues.get(0).getCtwo()) == 0.0f && Float.parseFloat(entryValues.get(0).getCthree()) == 0.0f &&
//                    Float.parseFloat(entryValues.get(0).getCfour()) == 0.0f) {
//                chart.setVisibility(View.GONE);
//                txtView_homechartEmpty.setVisibility(View.VISIBLE);
//                txtView_homechartEmpty.setText(leadStatusEmpty);
//            } else {
//                chart.setVisibility(View.VISIBLE);
//                txtView_homechartEmpty.setVisibility(View.GONE);
//                setData(entryArrayList);
//                chart.setCenterText(BikeStatus);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void initChartView() {
        colorsArray = new Integer[]{
                getResources().getColor(R.color.c1), getResources().getColor(R.color.c2), getResources().getColor(R.color.c3),
                getResources().getColor(R.color.c4), getResources().getColor(R.color.c5)

        };

        for (int i = 0; i < colorsArray.length; i++) {
            LegendModel legend = new LegendModel();
            legend.setLegendColor(colorsArray[i]);
            legend.setLegendName(parties[i]);
            legendArrayList.add(legend);
        }

        getValuefromservices(legendArrayList);

        //  chart = findViewById(R.id.chart1);
        // setData(5, 10F);


        tfRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins-Regular.ttf");
        tfLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins-Medium.ttf");
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 0, 5, 0);
        chart.getLegend().setEnabled(false);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);
        chart.setCenterTextColor(getResources().getColor(R.color.white));

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(getResources().getColor(R.color.toolBar));

        chart.setTransparentCircleColor(R.color.toolBar);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(75f);
        //  chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);


        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //  chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(10f);
        l.setXOffset(10f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(getResources().getColor(R.color.white));
        l.setTextSize(15F);
        l.setFormSize(18F);


        // entry label styling
        chart.setEntryLabelColor(getResources().getColor(R.color.white));
        chart.setEntryLabelTypeface(tfRegular);
        /*Chart Entry c1, c2, c3 values hidden here*/
        chart.setEntryLabelTextSize(0f);

    }

    private void setData(ArrayList<PieEntry> entries) {


        //initChartView();

        float range = 10F;
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        /*for (int i = 0; i < entries.size() ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length],
                    getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }*/


        //  PieDataSet dataSet = new PieDataSet(entries, "");
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(1f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        try {

            for (int i = 0; i < colorsArray.length; i++) {
                if (entries.get(i).getValue() == 0) {
                    colors.add(getResources().getColor(R.color.toolBar));
                } else {
                    colors.add(colorsArray[i]);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(15f);
        data.setValueTextColors(colors);
        data.setValueTypeface(tfLight);

        //  dataSet.setSliceSpace(0f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(ColorTemplate.COLOR_NONE);


        dataSet.setValueLinePart1OffsetPercentage(90f); /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
        dataSet.setValueLinePart1Length(0.1f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
        dataSet.setValueLinePart2Length(0.1f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */
        dataSet.isUsingSliceColorAsValueLineColor();
        dataSet.setUsingSliceColorAsValueLineColor(true);
        chart.setExtraOffsets(5, 0, 5, 0);


        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void getValuefromservices(ArrayList<LegendModel> legendArrayList) {

        try {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            int length = legendArrayList.size();
            for (int i = 0; i < length; i++) {
                final View view = inflater.inflate(R.layout.home_legend_single_item, null);
                AppCompatImageView linearColor = view.findViewById(R.id.linearColor);
                TextView linearName = view.findViewById(R.id.linearName);
                GradientDrawable gradientDrawable = (GradientDrawable) linearColor.getDrawable();
                gradientDrawable.setColor(legendArrayList.get(i).getLegendColor());

                linearName.setText(legendArrayList.get(i).getLegendName());
                homelayout_legend.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
