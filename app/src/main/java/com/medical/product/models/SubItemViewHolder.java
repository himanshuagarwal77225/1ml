package com.medical.product.models;

import android.view.View;
import android.widget.TextView;

import com.medical.product.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


public class SubItemViewHolder extends ChildViewHolder {

    private TextView childTextView;

    public SubItemViewHolder(View itemView) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_sub_item_name);
    }

    public void setSubItemName(String name) {
        childTextView.setText(name);
    }
}
