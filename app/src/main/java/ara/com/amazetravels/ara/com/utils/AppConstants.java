package ara.com.amazetravels.ara.com.utils;

/**
 * Created by sathi on 28-03-2018.
 */

public class AppConstants {
//    public static final String URL_FEED="http://arasoftwares.in/atnc-app/android_atncfile.php?action=";
//    public static final String ADD_CUSTOMER_API="booking";
//    public static String getAddCustomerUrl(){
//        return URL_FEED+ADD_CUSTOMER_API;
//    }

    public static final int SEND_OBJECT = 1;
    public static final int SEND_PARAM = 2;

    public static final String URL_FEED = "http://arasoftwares.in/atnc-app/android_atncfile.php?action=";
    public static final String ADD_CUSTOMER_API = "customersignup";

    public static String getAddCustomerUrl() {
        return URL_FEED + ADD_CUSTOMER_API;
    }
}

