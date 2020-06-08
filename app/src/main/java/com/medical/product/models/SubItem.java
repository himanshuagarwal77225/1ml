package com.medical.product.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubItem implements Parcelable {

    private String name;

    public SubItem(String name) {
        this.name = name;

    }
    protected SubItem(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubItem)) return false;

        SubItem subItem = (SubItem) o;
        return getName() != null ? getName().equals(subItem.getName()) : subItem.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubItem> CREATOR = new Creator<SubItem>() {
        @Override
        public SubItem createFromParcel(Parcel in) {
            return new SubItem(in);
        }

        @Override
        public SubItem[] newArray(int size) {
            return new SubItem[size];
        }
    };
}
