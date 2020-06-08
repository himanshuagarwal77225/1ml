
package com.medical.product.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OFFER {

    @SerializedName("Additional_Tests")
    @Expose
    private String additionalTests;
    @SerializedName("Booked_Count")
    @Expose
    private String bookedCount;
    @SerializedName("HCRInclude")
    @Expose
    private Integer hCRInclude;
    @SerializedName("New")
    @Expose
    private String _new;
    @SerializedName("aliasName")
    @Expose
    private String aliasName;
    @SerializedName("ben_max")
    @Expose
    private String benMax;
    @SerializedName("ben_min")
    @Expose
    private String benMin;
    @SerializedName("ben_multiple")
    @Expose
    private String benMultiple;
    @SerializedName("childs")
    @Expose
    private List<Child> childs = null;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("disease_group")
    @Expose
    private String diseaseGroup;
    @SerializedName("edta")
    @Expose
    private String edta;
    @SerializedName("fasting")
    @Expose
    private String fasting;
    @SerializedName("fluoride")
    @Expose
    private String fluoride;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("hc")
    @Expose
    private String hc;
    @SerializedName("image_location")
    @Expose
    private Object imageLocation;
    @SerializedName("image_master")
    @Expose
    private List<ImageMaster> imageMaster = null;
    @SerializedName("margin")
    @Expose
    private String margin;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("normal_val")
    @Expose
    private String normalVal;
    @SerializedName("ownpkg")
    @Expose
    private String ownpkg;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("rate")
    @Expose
    private Rate rate;
    @SerializedName("serum")
    @Expose
    private String serum;
    @SerializedName("specimen_type")
    @Expose
    private String specimenType;
    @SerializedName("test_count")
    @Expose
    private String testCount;
    @SerializedName("testnames")
    @Expose
    private String testnames;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("units")
    @Expose
    private String units;
    @SerializedName("urine")
    @Expose
    private String urine;
    @SerializedName("valid_to")
    @Expose
    private String validTo;
    @SerializedName("volume")
    @Expose
    private String volume;

    public String getAdditionalTests() {
        return additionalTests;
    }

    public void setAdditionalTests(String additionalTests) {
        this.additionalTests = additionalTests;
    }

    public String getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(String bookedCount) {
        this.bookedCount = bookedCount;
    }

    public Integer getHCRInclude() {
        return hCRInclude;
    }

    public void setHCRInclude(Integer hCRInclude) {
        this.hCRInclude = hCRInclude;
    }

    public String getNew() {
        return _new;
    }

    public void setNew(String _new) {
        this._new = _new;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getBenMax() {
        return benMax;
    }

    public void setBenMax(String benMax) {
        this.benMax = benMax;
    }

    public String getBenMin() {
        return benMin;
    }

    public void setBenMin(String benMin) {
        this.benMin = benMin;
    }

    public String getBenMultiple() {
        return benMultiple;
    }

    public void setBenMultiple(String benMultiple) {
        this.benMultiple = benMultiple;
    }

    public List<Child> getChilds() {
        return childs;
    }

    public void setChilds(List<Child> childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiseaseGroup() {
        return diseaseGroup;
    }

    public void setDiseaseGroup(String diseaseGroup) {
        this.diseaseGroup = diseaseGroup;
    }

    public String getEdta() {
        return edta;
    }

    public void setEdta(String edta) {
        this.edta = edta;
    }

    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    public String getFluoride() {
        return fluoride;
    }

    public void setFluoride(String fluoride) {
        this.fluoride = fluoride;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHc() {
        return hc;
    }

    public void setHc(String hc) {
        this.hc = hc;
    }

    public Object getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(Object imageLocation) {
        this.imageLocation = imageLocation;
    }

    public List<ImageMaster> getImageMaster() {
        return imageMaster;
    }

    public void setImageMaster(List<ImageMaster> imageMaster) {
        this.imageMaster = imageMaster;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalVal() {
        return normalVal;
    }

    public void setNormalVal(String normalVal) {
        this.normalVal = normalVal;
    }

    public String getOwnpkg() {
        return ownpkg;
    }

    public void setOwnpkg(String ownpkg) {
        this.ownpkg = ownpkg;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public String getSerum() {
        return serum;
    }

    public void setSerum(String serum) {
        this.serum = serum;
    }

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getTestCount() {
        return testCount;
    }

    public void setTestCount(String testCount) {
        this.testCount = testCount;
    }

    public String getTestnames() {
        return testnames;
    }

    public void setTestnames(String testnames) {
        this.testnames = testnames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUrine() {
        return urine;
    }

    public void setUrine(String urine) {
        this.urine = urine;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString()
    {
        return "Offer \n[New = "+_new+", HCRInclude = "
                +hCRInclude+", Additional_Tests = "+additionalTests+
                ", code = "+code+", testnames = "+testnames+
                ", urine = "+urine+", ben_max = "+benMax+
                ", fluoride = "+fluoride+", fasting = "+fasting+
                ", units = "+units+", type = "+type+", childs = "
                +childs+", disease_group = "+diseaseGroup+", rate = "
                +rate+", valid_to = "+validTo+", image_location = "+imageLocation+
                ", pay_type = "+payType+", aliasName = "+aliasName+", margin = "+margin+", edta = "+edta+
                ", group_name = "+groupName+", normal_val = "+normalVal+", test_count = "+testCount+
                ", ben_min = "+benMin+", image_master = "+imageMaster+", ben_multiple = "+benMultiple+
                ", ownpkg = "+ownpkg+", volume = "+volume+", specimen_type = "+specimenType+", Booked_Count = "
                +bookedCount+", name = "+name+", hc = "+hc+", serum = "+serum+"]\n";
    }
}
