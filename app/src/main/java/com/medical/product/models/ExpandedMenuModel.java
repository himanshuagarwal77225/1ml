package com.medical.product.models;

import java.util.ArrayList;
import java.util.List;

public class ExpandedMenuModel {

    String iconName = "";
    int iconImg = -1; // menu icon resource id
    List<String> heading1myaccount ;
    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public int getIconImg() {
        return iconImg;
    }
    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }

    public List<String> getHeading1myaccount() {
        return heading1myaccount;
    }

    public void setHeading1myaccount(List<String> heading1myaccount) {
        this.heading1myaccount = heading1myaccount;
    }
}
