package com.medical.product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.product.R;
import com.medical.product.models.Item;
import com.medical.product.models.ItemViewHolder;
import com.medical.product.models.SubItem;
import com.medical.product.models.SubItemViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class ItemAdapter extends ExpandableRecyclerViewAdapter<ItemViewHolder, SubItemViewHolder> {

    public ItemAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public SubItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sub_item, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubItemViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final SubItem subItem = ((Item) group).getItems().get(childIndex);
        holder.setSubItemName(subItem.getName());
    }

    @Override
    public void onBindGroupViewHolder(ItemViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setItemTitle(group);
    }
}
