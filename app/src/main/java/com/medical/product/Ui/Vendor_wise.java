package com.medical.product.Ui;

import com.medical.product.models.GatterGetCartList;

import java.util.ArrayList;

public class Vendor_wise {
    String vendor_id;
    ArrayList<GatterGetCartList> filelist;

    public Vendor_wise(String vendor_id, ArrayList<GatterGetCartList> filelist) {
        this.vendor_id = vendor_id;
        this.filelist = filelist;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public ArrayList<GatterGetCartList> getFilelist() {
        return filelist;
    }

    public void setFilelist(ArrayList<GatterGetCartList> filelist) {
        this.filelist = filelist;
    }
}
