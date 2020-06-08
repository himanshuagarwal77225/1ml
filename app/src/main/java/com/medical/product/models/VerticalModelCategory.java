package com.medical.product.models;

import java.util.ArrayList;

public class VerticalModelCategory {

    String title;


    ArrayList<HorizontalModelCategory> arrayListCategory;

    ArrayList<HorizontalModelProducts> arrayListProducts;

    public ArrayList<HorizontalModelProducts> getArrayListProducts() {
        return arrayListProducts;
    }

    public void setArrayListProducts(ArrayList<HorizontalModelProducts> arrayListProducts) {
        this.arrayListProducts = arrayListProducts;
    }

    public ArrayList<HorizontalModelCategory> getArrayListCategory() {
        return arrayListCategory;
    }

    public void setArrayListCategory(ArrayList<HorizontalModelCategory> arrayListCategory) {
        this.arrayListCategory = arrayListCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
