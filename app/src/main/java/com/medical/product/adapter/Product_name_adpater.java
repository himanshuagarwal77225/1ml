package com.medical.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medical.product.R;

import java.util.ArrayList;
import java.util.List;

public class Product_name_adpater extends BaseAdapter implements Filterable {
    private Activity context;
    private ArrayList<String> emplist;
    ArrayList<String> filteremplist;

    public Product_name_adpater(Activity applicationContext, ArrayList<String> list) {
        this.context = applicationContext;
        this.emplist = list;
        this.filteremplist = list;
    }

    @Override
    public int getCount() {
        return emplist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int positon, View listview, ViewGroup viewGroup) {
        final  Holder holder;
        LayoutInflater layoutInflater;
        if (listview == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listview = layoutInflater.inflate(R.layout.row_name, viewGroup, false);
            holder = new Holder();
            listview.setTag(holder);
            holder.name = (TextView) listview.findViewById(R.id.name);
            holder.itemview = (LinearLayout) listview.findViewById(R.id.itemview);
            holder.name.setText(emplist.get(positon));
            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("name",  holder.name.getText().toString());
                    context.setResult(1, intent);
                    context.finish();
                }
            });
        } else {
            holder = (Holder) listview.getTag();
        }
        return listview;
    }

    private static class Holder {
        TextView name;
        LinearLayout itemview;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    emplist = filteremplist;
                } else {
                    List<String> filteredList = new ArrayList<>();
                    if (filteremplist != null) {
                        for (String row : filteremplist) {
                            if (row.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        emplist = (ArrayList<String>) filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = emplist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                emplist = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

