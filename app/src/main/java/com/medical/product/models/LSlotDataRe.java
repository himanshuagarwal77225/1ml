package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LSlotDataRe {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Slot")
    @Expose
    private String slot;
    @SerializedName("SlotMasterId")
    @Expose
    private String slotMasterId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getSlotMasterId() {
        return slotMasterId;
    }

    public void setSlotMasterId(String slotMasterId) {
        this.slotMasterId = slotMasterId;
    }

}
