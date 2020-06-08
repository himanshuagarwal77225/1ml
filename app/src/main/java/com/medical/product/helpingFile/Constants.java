package com.medical.product.helpingFile;

public class Constants {
    public static final String PREF_NAME = "oneml";
    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final int REQUEST_CAMERA = 113;
    public static final int FILE_CHOOSER_CODE = 101;
    public static final int CAMERA_PERMISSION = 110;
    public static final int REQUEST_READ_STORAGE = 116;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 20;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 21;

    public static final int LOGIN_TYPE_EMAIL = 0;
    public static final int LOGIN_TYPE_MOBILE = 1;
    public static final int LOGIN_TYPE_GOOGLE = 2;
    public static final int LOGIN_TYPE_FACEBOOK = 3;

    public static final String FILE_TYPE_PPT = "ppt";
    public static final String FILE_TYPE_PDF = "pdf";
    public static final String FILE_TYPE_DOC = "doc";
    public static final String FILE_TYPE_VIDEO = "mp3";
    public static final String FILE_TYPE_PNG = "png";
    public static final String FILE_TYPE_JPEG = "jpeg";
    public static final String FILE_TYPE_DOCX = "docx";


    /**
     * Default number to set the image on the top of the textView
     */
    public static final int DEFAULT_NUMBER = 0;

    public enum SHARED_PREF_CONST {
        user_id,
        name,
        password,
        lang,
        file,
        phone,
        email,
        logInStatus,
        token,
        bearer_token,
        count,
        fcm_token,
        user_id_1ml,
        user_profile,
        latitude,
        longitude
    }



}
