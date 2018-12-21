package com.tvscs.ilead.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tvscs.ilead.model.ProductResponse;
import com.tvscs.ilead.utils.AdapterCallback;
import com.tvscs.ilead.utils.Constants;

import java.util.ArrayList;

import ilead.tvscs.com.ilead.R;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.DataObjectHolder> implements Constants {
    @SuppressWarnings("unused")
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<ProductResponse> mDataset;
    private Context mContext;
    private AdapterCallback callback;
    private boolean flag = false;
    private int selected_position = -1;

    public ProductListAdapter(ArrayList<ProductResponse> myDataset, Context context, AdapterCallback adapterCallback) {
        mDataset = myDataset;
        this.mContext = context;
        callback = adapterCallback;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.product_adapterview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public ProductResponse getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        ProductResponse mProductResponse = getItem(position);
        String backgroundImageName = String.valueOf(mProductResponse.getSzproductcode());

        String[] productName = mProductResponse.getSzdesc().split("-");
        holder.product_textView.setText(productName[1].toUpperCase());

        if (mProductResponse.isSelected()) {
            switch (backgroundImageName) {
                case AL:
                    holder.product_imageButton.setImageResource(R.mipmap.auto_selected);
                    break;
                case AP:
                    holder.product_imageButton.setImageResource(R.mipmap.usedtractor_selected);
                    break;
                case CA:
                    holder.product_imageButton.setImageResource(R.mipmap.car_selected);
                    break;
                case CD:
                    holder.product_imageButton.setImageResource(R.mipmap.commercial_selected);
                    break;
                case DC:
                    holder.product_imageButton.setImageResource(R.mipmap.car_selected);
                    break;
                case LN:
                    holder.product_imageButton.setImageResource(R.mipmap.usedtractor_selected);
                    break;
                case TR:
                    holder.product_imageButton.setImageResource(R.mipmap.tractor_selected);
                    break;
                case TW:
                    holder.product_imageButton.setImageResource(R.mipmap.bike_selected);
                    break;
                case UT:
                    holder.product_imageButton.setImageResource(R.mipmap.usedbike_selected);
                    break;
                case UV:
                    holder.product_imageButton.setImageResource(R.mipmap.commercial_selected);
                    break;
                default:
                    holder.product_imageButton.setImageResource(R.mipmap.auto_selected);
                    break;
            }

        } else {
//SetUnselectedIMAGE
            switch (backgroundImageName) {
                case AL:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_auto);
                    break;
                case AP:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_usedtractor);
                    break;
                case CA:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_car);
                    break;
                case CD:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_commercial);
                    break;
                case DC:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_car);
                    break;
                case LN:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_usedtractor);
                    break;
                case TR:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_tractor);
                    break;
                case TW:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_bike);
                    break;
                case UT:
                    holder.product_imageButton.setImageResource(R.drawable.ic_usedbike);
                    break;
                case UV:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_commercial);
                    break;
                default:
                    holder.product_imageButton.setImageResource(R.drawable.ic_trans_auto);
                    break;
            }
        }

        holder.product_imageButton.setId(position);
        holder.product_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
                ProductResponse mProductResponse = getItem(position);
                unSelect();
                mProductResponse.setSelected(true);
                notifyDataSetChanged();
                int pos = position;
                callback.onClickCallback(pos, mProductResponse);
            }
        });
    }

    public void addItem(ProductResponse dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void unSelect() {
        for (ProductResponse mProductResponse : mDataset) {
            mProductResponse.setSelected(false);
        }
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        ImageButton product_imageButton;
        TextView product_textView;

        private DataObjectHolder(View itemView) {
            super(itemView);
            product_imageButton = (ImageButton) itemView.findViewById(R.id.product_imageButton);
            product_textView = (TextView) itemView.findViewById(R.id.product_textView);
        }
    }
}