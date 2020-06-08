package com.medical.product.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class Item extends ExpandableGroup<SubItem> {

    private String title;
    private ArrayList<SubItem> items = new ArrayList<>();

    public Item(String title, List<SubItem> items) {
        super(title, items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Item;

    }

}

