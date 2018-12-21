package com.tvscs.ilead.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tvscs.ilead.model.DealersPojo;

import java.util.ArrayList;
import java.util.List;

import ilead.tvscs.com.ilead.R;

public class DealersAdapter extends RecyclerView.Adapter<DealersAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<DealersPojo> dealersList;
    private List<DealersPojo> dealerListFiltered;
    private ContactsAdapterListener listener;

    public DealersAdapter(Context context, ArrayList<DealersPojo> dealersPojoArrayList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.dealersList = dealersPojoArrayList;
        this.dealerListFiltered = dealersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dealer_adapter_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DealersPojo contact = dealerListFiltered.get(position);
        holder.dealerName.setText(contact.getDealerName());
    }

    @Override
    public int getItemCount() {
        return dealerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dealerListFiltered = dealersList;
                } else {
                    List<DealersPojo> filteredList = new ArrayList<>();
                    for (DealersPojo row : dealersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDealerName().toLowerCase().contains(charString.toLowerCase()) || row.getDealercode().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dealerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dealerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dealerListFiltered = (ArrayList<DealersPojo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(DealersPojo contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dealerName;

        public MyViewHolder(View view) {
            super(view);
            dealerName = view.findViewById(R.id.nameTxt);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(dealerListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}




































/*extends RecyclerView.Adapter<DealersAdapter.MyHolder> {

    private Context c;
    private ArrayList<String> dealersList;

    public DealersAdapter(Context c, ArrayList<String> list) {
            this.c = c;
            this.dealersList = list;
            }

    //INITIALIE VH
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_adapter_layout,parent,false);
            MyHolder holder=new MyHolder(v);
            return holder;
            }

    //BIND DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
            holder.nameTxt.setText(dealersList.get(position));
            }

    @Override
    public int getItemCount() {
            return dealersList.size();
            }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;

        public MyHolder(View itemView) {
            super(itemView);
            nameTxt= (TextView) itemView.findViewById(R.id.nameTxt);
        }
    }
}*/