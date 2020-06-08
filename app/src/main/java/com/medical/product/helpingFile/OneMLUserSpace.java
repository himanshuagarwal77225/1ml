package com.medical.product.helpingFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.product.models.UserModel;

import org.apache.commons.lang3.StringUtils;


public class OneMLUserSpace {

    private static final String TAG = OneMLUserSpace.class.getName();

    private static OneMLUserSpace _yolearn;
    private Context context;
    private SharedPreferences sharedPreferences;

    private OneMLUserSpace(@NonNull Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
       /* if (!Util.validateTextNotEmpty(getEmail())) {
            SharedPreferences oldSharedPreferences = context.getSharedPreferences(Constants.OLD_PREF_NAME, Context.MODE_PRIVATE);
            String email = getOldEmail(oldSharedPreferences);
            setEmail(email);
        }

        if (!Util.validateTextNotEmpty(getPassword())) {
            SharedPreferences oldSharedPreferences = context.getSharedPreferences(Constants.OLD_PREF_NAME, Context.MODE_PRIVATE);
            String password = getOldUserPassword(oldSharedPreferences);
            setPassword(password);
        }*/
    }

    public static OneMLUserSpace getInstance(@NonNull Context context) {
        initialize(context);
        return _yolearn;
    }

    public static void initialize(@NonNull Context context) {
        if (_yolearn == null) {
            synchronized (OneMLUserSpace.class) {
                _yolearn = new OneMLUserSpace(context);
            }
        }
    }

    @Nullable
    public String getToken() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.token.name(), StringUtils.EMPTY);
    }

    public void setToken(String deviceToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.token.name(), deviceToken);
        editor.apply();
    }

    @Nullable
    public String getBearerToken() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.bearer_token.name(), StringUtils.EMPTY);
    }

    public void setBearerToken(String bearerToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.bearer_token.name(), bearerToken);
        editor.apply();
    }

    @Nullable
    public String getFCMToken() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.fcm_token.name(), StringUtils.EMPTY);
    }

    public void setFCMToken(String fcmToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.fcm_token.name(), fcmToken);
        editor.apply();
    }

    @Nullable
    public String getLastLatitude() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.latitude.name(), StringUtils.EMPTY);
    }

    public void setLastLatitude(String latitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.latitude.name(), latitude);
        editor.apply();
    }

    @Nullable
    public String getLastLongitude() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.longitude.name(), StringUtils.EMPTY);
    }

    public void setLastLongitude(String longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.longitude.name(), longitude);
        editor.apply();
    }

    @Nullable
    public String get1MLUserID() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.user_id_1ml.name(), StringUtils.EMPTY);
    }

    public void set1MLUserID(String userID1ML) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.user_id_1ml.name(), userID1ML);
        editor.apply();
    }

    @Nullable
    public String getUserID() {
        return sharedPreferences.getString(Constants.SHARED_PREF_CONST.user_id.name(), StringUtils.EMPTY);
    }

    public void setUserID(String userID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_CONST.user_id.name(), userID);
        editor.apply();
    }

    public UserModel getUserProfile() {
        UserModel userModel = null;
        String data = sharedPreferences.getString(Constants.SHARED_PREF_CONST.user_profile.name(), null);
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (data !=null && !TextUtils.isEmpty(data)) {
                userModel = mapper.readValue(data, UserModel.class);
            }
        } catch (JsonProcessingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return userModel;
    }

    public void setUserProfile(UserModel userProfile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(userProfile);
            editor.putString(Constants.SHARED_PREF_CONST.user_profile.name(), data);
        } catch (JsonProcessingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        editor.apply();
    }



     /*public AllCategoryResponseModel getAllCategory() {
        AllCategoryResponseModel allCategoryResponseModel = new AllCategoryResponseModel();
        String data = sharedPreferences.getString(Constants.SHARED_PREF_CONST.all_category.name(), null);
        if (!HelperFunction.checkStringEmpty(data)) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            allCategoryResponseModel = mapper.readValue(data, AllCategoryResponseModel.class);
        } catch (JsonProcessingException e) {
            Timber.e(e);
        } catch (IOException e) {
            Timber.e(e);
        }
        return allCategoryResponseModel;
    }

    public void setAllCategory(AllCategoryResponseModel allCategoryResponseModel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(allCategoryResponseModel);
            editor.putString(Constants.SHARED_PREF_CONST.all_category.name(), data);
        } catch (JsonProcessingException e) {
            Timber.e(e);
        }
        editor.apply();
    }*/

    public int getLogInStatus() {
        return sharedPreferences.getInt(Constants.SHARED_PREF_CONST.logInStatus.name(), -1);
    }

    public void setLogInStatus(int status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.SHARED_PREF_CONST.logInStatus.name(), status);
        editor.apply();
    }

    public void resetData() {
        long id = -1;
        setFCMToken(null);
        setToken(null);
        setBearerToken(null);
        setUserProfile(null);
        setLogInStatus(-1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        /*setUserId(0);
        setName(null);
        setEmail(null);
        setPhone(null);
        setLogInStatus(-1);
        setToken(null);
        setImage(null);
        setPassword(null);*/
    }
}
