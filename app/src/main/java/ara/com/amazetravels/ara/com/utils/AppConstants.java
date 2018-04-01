package ara.com.amazetravels.ara.com.utils;

import android.util.Log;

import org.json.JSONObject;

import ara.com.amazetravels.ara.com.amazetravels.models.User;

/**
 * Created by sathi on 28-03-2018.
 */

public class AppConstants {

    public static final String URL_FEED = "http://arasoftwares.in/atnc-app/android_atncfile.php?action=";
    public static final String ADD_CUSTOMER_API = "customersignup";
    public static final String USER_VALIDATE_API = "customerlogin";
    private static final String VEHICLE_TYPE_API = "vehiclelist";
    private static final String BOOKING_API="booking";

    public static String getAddCustomerUrl() {
        return URL_FEED + ADD_CUSTOMER_API;
    }

    public static String getUserValidateUrl() {
        return URL_FEED + USER_VALIDATE_API;
    }

    public static String getVehicleTypeApi() {
        return URL_FEED + VEHICLE_TYPE_API;
    }
    public static String getBookingApi() {
        return URL_FEED + BOOKING_API;
    }


    public static String VEHICLE_TYPE_ID = "vehicle_type_id";
    public static String VEHICLE_NAME = "vehicle_type_name";
    public static String SUCCESS_MESSAGE = "success";
    public static String LOGIN_RESULT = "login";
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "username";
    public static final String MOBILE_NUMBER="mobileno";


    private static User user;

    public static User getCurrentUser() {
        return user;
    }

    public static void setCustomer(User user) {
        AppConstants.user = user;
    }

    public static void setCustomer(JSONObject jsonObject) {
        setCustomer(getUser(jsonObject));
    }

    public static User getUser(JSONObject jsonObject) {
        User user = null;
        try {

            int id = jsonObject.getInt(AppConstants.USER_ID);
            String name = jsonObject.getString(AppConstants.USER_NAME);
            String mobileNo=jsonObject.getString(AppConstants.MOBILE_NUMBER);
            user = new User(id, name,mobileNo);
        } catch (Exception e) {
            Log.e("Register Login User", e.getMessage());
        }
        return user;
    }

}

