package com.medical.product.helpingFile;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.medical.product.Ui.Fragment_Order_Product_List;
import com.medical.product.Ui.Fragment_Prescription_Order_List;

public class PagerOrderType extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PagerOrderType(FragmentManager fm, int tabCount) {
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
                Fragment_Order_Product_List tab1 = new Fragment_Order_Product_List();
                return tab1;
            case 1:
                Fragment_Prescription_Order_List tab2 = new Fragment_Prescription_Order_List();
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