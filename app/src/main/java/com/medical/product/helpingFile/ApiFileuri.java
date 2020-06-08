package com.medical.product.helpingFile;

public class ApiFileuri {
    //main  base url for app
    private static final String ROOT_URL_OLINE = "http://www.1ml.co.in/demo/";
    private static final String ROOT_URL = ROOT_URL_OLINE + "user/";
    public static final String ROOT_URL_deep = "http://www.1ml.co.in";
    public static final String CreateAccount = ROOT_URL + "set";
    public static final String ROOT_URL_CATEGORY_IMAGE = ROOT_URL_OLINE + "attachments/category_images/";
    public static final String ROOT_URL_advertisement_IMAGE = ROOT_URL_OLINE + "attachments/advertisement/";
    public static final String ROOT_URL_BRANDS_IMAGE = ROOT_URL_OLINE + "attachments/brand_images/";
    public static final String ROOT_URL_PRODUCT_IMAGE = ROOT_URL_OLINE + "attachments/product_images/";
    public static final String ROOT_URL_USER_IMAGE = ROOT_URL_OLINE + "attachments/user_images/";
    public static final String ROOT_VENDOR_IMAGE = ROOT_URL_OLINE + "attachments/vendor_images/";
    public static final String UPLOADPROFILEIMAGE = ROOT_URL + "Api/UploadProfileImage";


    public static final int REQCODE = 100;
    public static final String imageList = "imageList";
    public static final String imageName = "name";


    //  public static final String ROOT_HTTP_URL = ROOT_URL+"set";

    public static String ROOT_HTTP_URL = ROOT_URL_OLINE + "api/";
    public static String Checkout = ROOT_URL_OLINE + "api/product/order-place";
    public static String cacnel = ROOT_URL_OLINE + "api/product/order-cancel";
    public static String review = ROOT_URL_OLINE + "api/product/send-review";
    public static String feedback = ROOT_URL_OLINE + "api/user/feedback";
    public static String get_shiping = ROOT_URL_OLINE + "api/product/shipping-charge";
    public static String get_discount = ROOT_URL_OLINE + "api/product/discount";
    public static String perception_orders = ROOT_URL_OLINE + "api/get-perception-orders";
    public static String accept_perception = ROOT_URL_OLINE + "api/accept-perception";
    public static String notifications = ROOT_URL_OLINE + "api/notification";
    public static String filter = ROOT_URL_OLINE + "api/product/filter";
    public static String produ = ROOT_URL_OLINE + "api/product/admin-product";
    public static String seller = ROOT_URL_OLINE + "api/product/seller-product";
    public static String  prescription = ROOT_URL_OLINE + "api/user-prescription";


}
