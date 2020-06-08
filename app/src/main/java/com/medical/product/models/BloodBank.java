package com.medical.product.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BloodBank {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("blood-banks")
    @Expose
    private List<BloodBankChild> bloodBanks = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<BloodBankChild> getBloodBanks() {
        return bloodBanks;
    }

    public void setBloodBanks(List<BloodBankChild> bloodBanks) {
        this.bloodBanks = bloodBanks;
    }

}