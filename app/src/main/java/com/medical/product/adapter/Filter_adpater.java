package com.medical.product.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Ui.Filetr_selector;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.models.Filter_data;

import java.util.ArrayList;

public class Filter_adpater extends RecyclerView.Adapter<Filter_adpater.HorizontalRVViewHolder> {
    private Keystore store;
    Context context;
    ArrayList<Filter_data> arrayList;
    Filetr_selector filetr_selector;

    public Filter_adpater(ArrayList<Filter_data> arrayList, Filetr_selector filetr_selector, Context context) {
        this.context = context;
        this.arrayList = arrayList;
        this.filetr_selector = filetr_selector;
    }


    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_row, parent, false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final Filter_data filter = arrayList.get(position);
        holder.lable.setText(filter.getValue());
        if (filter.isIs_checked()) {
            holder.checked.setChecked(true);
        }
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filetr_selector.selected_value(filter.getKey(), filter.getValue());

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checked.setChecked(true);
                filetr_selector.selected_value(filter.getKey(), filter.getValue());
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView lable;
        CheckBox checked;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            lable = (TextView) itemView.findViewById(R.id.lable);
            checked = (CheckBox) itemView.findViewById(R.id.checked);
        }


    }
}
