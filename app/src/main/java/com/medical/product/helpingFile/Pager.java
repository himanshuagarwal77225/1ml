package com.medical.product.helpingFile;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.medical.product.Ui.Fragment_Family_Document;
import com.medical.product.Ui.Fragment_Family_Personal_Detail;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Fragment_Family_Personal_Detail tab1 = new Fragment_Family_Personal_Detail();
                return tab1;
            case 1:
                Fragment_Family_Document tab2 = new Fragment_Family_Document();
                return tab2;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}