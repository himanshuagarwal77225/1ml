package com.medical.product.models;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.medical.product.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;


import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ItemViewHolder extends GroupViewHolder {

    private TextView itemName;
    private ImageView arrow;
    public ItemViewHolder(View itemView) {
        super(itemView);
        itemName = (TextView) itemView.findViewById(R.id.list_item_name);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_arrow);
    }

    public void setItemTitle(ExpandableGroup genre) {
        if (genre instanceof Item) {
            itemName.setText(genre.getTitle());
        }

    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}