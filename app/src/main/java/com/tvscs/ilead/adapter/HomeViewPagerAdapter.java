package com.tvscs.ilead.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
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
import com.tvscs.ilead.model.LegendModel;
import com.tvscs.ilead.model.homechartmodel.RefConenewResult;
import com.tvscs.ilead.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ilead.tvscs.com.ilead.R;

public class HomeViewPagerAdapter extends PagerAdapter implements Constants {
    protected final String[] parties = new String[]{
            "C0", "C1", "C2", "C3", "C4"};
    protected Integer[] colorsArray;
    protected Typeface tfRegular;
    protected Typeface tfLight;
    protected Typeface tfBold;
    ViewHolder holder;
    private Context mContext;
    private ArrayList<LegendModel> legendArrayList = new ArrayList<>();
    private ArrayList<RefConenewResult> homeChartsModel;
//    private List<Lstuserdtls> entryValues = new ArrayList<>();

    public HomeViewPagerAdapter(Context mContext, ArrayList<RefConenewResult> homeChartsModel) {
        this.mContext = mContext;
        this.homeChartsModel = homeChartsModel;

        Log.e("ArrayList", "ArrayList<fgfgfgfgfg>" + homeChartsModel);
    }

    @Override
    public int getCount() {
        if (homeChartsModel.size() == 0) {
            return 1; 
        } else {
            return homeChartsModel.size();
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.chart_adapter, collection, false);
        holder = new ViewHolder(layout);
        layout.setTag(holder);
        initChartView();
        Log.e("getBikeLeadService", "getBikeLeadService" + homeChartsModel);
        getBikeLeadService(homeChartsModel, position);

        collection.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      //  super.destroyItem(container, position, object);
        ((ViewPager) container).removeView((View) object);

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
                holder.homelayout_legend.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBikeLeadService(ArrayList<RefConenewResult> homeChartsModel, int pos) {
        try {
//            entryValues.addAll(homeChartsModel);
            Log.e("entryValues", "entryValues" + homeChartsModel);
            ArrayList<PieEntry> entryArrayList = new ArrayList<>();

            if (!homeChartsModel.isEmpty()) {
                entryArrayList.add(new PieEntry(Float.parseFloat(homeChartsModel.get(pos).getCzero()), parties[0]));
                entryArrayList.add(new PieEntry(Float.parseFloat(homeChartsModel.get(pos).getCone()), parties[1]));
                entryArrayList.add(new PieEntry(Float.parseFloat(homeChartsModel.get(pos).getCtwo()), parties[2]));
                entryArrayList.add(new PieEntry(Float.parseFloat(homeChartsModel.get(pos).getCthree()), parties[3]));
                entryArrayList.add(new PieEntry(Float.parseFloat(homeChartsModel.get(pos).getCfour()), parties[4]));

           /* entryArrayList.add(new PieEntry(Float.parseFloat("210"), parties[0]));
            entryArrayList.add(new PieEntry(Float.parseFloat("1"), parties[1]));
            entryArrayList.add(new PieEntry(Float.parseFloat("2"), parties[2]));
            entryArrayList.add(new PieEntry(Float.parseFloat("1"), parties[3]));
            entryArrayList.add(new PieEntry(Float.parseFloat("2"), parties[4]));*/

                if (Float.parseFloat(homeChartsModel.get(pos).getCzero()) == 0.0f && Float.parseFloat(homeChartsModel.get(pos).getCone()) == 0.0f &&
                        Float.parseFloat(homeChartsModel.get(pos).getCtwo()) == 0.0f && Float.parseFloat(homeChartsModel.get(pos).getCthree()) == 0.0f &&
                        Float.parseFloat(homeChartsModel.get(pos).getCfour()) == 0.0f) {
                    holder.chart.setVisibility(View.GONE);
                    holder.txtView_homechartEmpty.setVisibility(View.VISIBLE);
                    holder.txtView_homechartEmpty.setTypeface(tfBold);
                    holder.txtView_homechartEmpty.setText(homeChartsModel.get(pos).getProductdesc() + "\n" + Notavailable);
                } else {
                    holder.chart.setVisibility(View.VISIBLE);
                    holder.txtView_homechartEmpty.setVisibility(View.GONE);
                    setData(entryArrayList);
                    holder.chart.setCenterText(homeChartsModel.get(pos).getProductdesc());
                }
            } else {
                holder.chart.setVisibility(View.GONE);
                holder.txtView_homechartEmpty.setVisibility(View.VISIBLE);
                holder.txtView_homechartEmpty.setTypeface(tfBold);
               // holder.txtView_homechartEmpty.setText(Notavailable);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initChartView() {
        colorsArray = new Integer[]{
                mContext.getResources().getColor(R.color.c1), mContext.getResources().getColor(R.color.c2), mContext.getResources().getColor(R.color.c3),
                mContext.getResources().getColor(R.color.c4), mContext.getResources().getColor(R.color.c5)};

        legendArrayList = new ArrayList<>();
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
        tfBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins-SemiBold.ttf");
        tfLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins-Medium.ttf");
        holder.chart.setUsePercentValues(false);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setExtraOffsets(5, 5, 5, 5);
        holder.chart.getLegend().setEnabled(false);

        holder.chart.setDragDecelerationFrictionCoef(0.95f);

        holder.chart.setCenterTextTypeface(tfBold);
        holder.chart.setCenterTextColor(mContext.getResources().getColor(R.color.white));

        holder.chart.setDrawHoleEnabled(true);
        holder.chart.setHoleColor(mContext.getResources().getColor(R.color.toolBar));

        holder.chart.setTransparentCircleColor(R.color.toolBar);
        holder.chart.setTransparentCircleAlpha(110);

        holder.chart.setHoleRadius(75f);
        //  chart.setTransparentCircleRadius(61f);

        holder.chart.setDrawCenterText(true);

        holder.chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        holder.chart.setRotationEnabled(false);
        holder.chart.setHighlightPerTapEnabled(false);


        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //  chart.setOnChartValueSelectedListener(this);

        holder.chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = holder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(10f);
        l.setXOffset(10f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(mContext.getResources().getColor(R.color.white));
        l.setTextSize(15F);
        l.setFormSize(18F);


        // entry label styling
        holder.chart.setEntryLabelColor(mContext.getResources().getColor(R.color.white));
        holder.chart.setEntryLabelTypeface(tfRegular);
        /*Chart Entry c1, c2, c3 values hidden here*/
        holder.chart.setEntryLabelTextSize(0f);

    }

    private void setData(ArrayList<PieEntry> entries) {

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(1f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        try {
            for (int i = 0; i < colorsArray.length; i++) {
                if (entries.get(i).getValue() == 0) {
                    colors.add(mContext.getResources().getColor(R.color.toolBar));
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
        dataSet.setValueLinePart1Length(0.15f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
        dataSet.setValueLinePart2Length(0.15f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */
        dataSet.isUsingSliceColorAsValueLineColor();
        dataSet.setUsingSliceColorAsValueLineColor(true);

        dataSet.setValueLinePart1Length(.10f);
        dataSet.setValueLinePart2Length(.50f);

        holder.chart.setExtraOffsets(5, 0, 5, 0);
        holder.chart.setData(data);
        holder.chart.highlightValues(null);
        holder.chart.invalidate();
    }

    class ViewHolder {
        @BindView(R.id.homechart)
        PieChart chart;
        @BindView(R.id.txtView_homechartEmpty)
        TextView txtView_homechartEmpty;
        @BindView(R.id.homelayout_legend)
        LinearLayout homelayout_legend;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

