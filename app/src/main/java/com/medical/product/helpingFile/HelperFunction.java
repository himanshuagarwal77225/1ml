package com.medical.product.helpingFile;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class HelperFunction {

    public static boolean checkStringEmpty(@Nullable String str) {
        return str != null && !TextUtils.isEmpty(str);
    }
}
