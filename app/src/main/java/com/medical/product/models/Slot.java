package com.medical.product.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {

    @SerializedName("LSlotDataRes")
    @Expose
    private List<LSlotDataRe> lSlotDataRes = null;

    public List<LSlotDataRe> getLSlotDataRes() {
        return lSlotDataRes;
    }

    public void setLSlotDataRes(List<LSlotDataRe> lSlotDataRes) {
        this.lSlotDataRes = lSlotDataRes;
    }

}