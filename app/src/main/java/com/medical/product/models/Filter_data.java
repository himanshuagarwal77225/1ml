package com.medical.product.models;

public class Filter_data {
    String key,value;
    boolean is_checked;

    public Filter_data(String key, String value, boolean is_checked) {
        this.key = key;
        this.value = value;
        this.is_checked = is_checked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}
