package ara.com.amazetravels.ara.com.utils;

import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import ara.com.amazetravels.ara.com.amazetravels.models.Customer;


/**
 * Created by sathi on 28-03-2018.
 */

public class AppConstants {
    public static final String SERVICE_PARAM_BOOKING_ID = "com.ara.amazetravels.utils.extra.booking_id";

    public static final String URL_FEED = "http://arasoftwares.in/atnc-app/android_atncfile.php?action=";

    public static final String ADD_CUSTOMER_API = "customersignup";
    public static final String USER_VALIDATE_API = "customerlogin";
    private static final String VEHICLE_TYPE_API = "vehiclelist";
    private static final String ACTION_HAS_CONFIRMED = "has_confirmed";
    private static final String ACTION_BOOKING_HISTORY = "booking_history";

    public static final String PARAM_HAS_CONFIRMED = "status";
    private static final String BOOKING_API = "booking";
    public static final int REQUEST_BOOKING = 0;
    public static final int REQUEST_VOICE_BOOKING = 1;
    public static final int REQUEST_LOGIN = 100;

    public static String getBookingHistoryAction(){
        return URL_FEED+ACTION_BOOKING_HISTORY;
    }

    public static String getConfirmedAction() {
        return URL_FEED + ACTION_HAS_CONFIRMED;
    }

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

    public static String PREFERENCE_NAME = "amaze_travels.ara";

    public static long COUNT_DOWN_INTERVAL = 1000;
    public static long AUDIO_BOOKING_TIME = 3 * 60 * 1000;
    public static int AUDIO_BOOKING_MIN = 3 * 60;


    public final static String VEHICLE_TYPE_ID = "vehicle_type_id";
    public final static String VEHICLE_NAME = "vehicle_type_name";
    public final static String SUCCESS_MESSAGE = "success";
    public final static String LOGIN_RESULT = "login";
    public final static String PARAM_STATUS = "status";
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "username";
    public static final String MOBILE_NUMBER = "mobileno";
    public static final String IDENTITY = "Identity";
    public static final String CUSTOMER_NAME = "name";
    public static final String PARAM_BOOKING_ID = "booking_id";
    public static final String CUSTOMER_ID = "customerId";


    private static Customer user;

    public static Customer getCurrentUser() {
        return user;
    }

    public static void setCustomer(Customer user) {
        AppConstants.user = user;
    }

    public static void setCustomer(JSONObject jsonObject) {
        setCustomer(getUser(jsonObject));
    }

    public static Customer getUser(JSONObject jsonObject) {
        Customer user = null;
        try {

            int id = jsonObject.getInt(AppConstants.USER_ID);
            String name = jsonObject.getString(AppConstants.USER_NAME);
            String mobileNo = jsonObject.getString(AppConstants.MOBILE_NUMBER);
            user = new Customer(id, name, mobileNo);
        } catch (Exception e) {
            Log.e("Register Login User", e.getMessage());
        }
        return user;
    }

    public static String getStringDate(Calendar calendar, boolean forDisplay) {
        if (forDisplay) {
            DateFormat dateFormat = DateFormat.getDateInstance();
            return dateFormat.format(calendar.getTime());
        } else {
            Date date = calendar.getTime();
            String strDate = calendar.get(Calendar.DATE) + "//" + calendar.get(Calendar.MONTH) + "//" +
                    calendar.get(Calendar.YEAR);
            return strDate;
        }
    }

    public static String getStringTime(Calendar calendar) {
        String am_pm = "";
        if (calendar.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + "";
        strHrsToShow = strHrsToShow + ":" + calendar.get(Calendar.MINUTE) + " " + am_pm;
        return strHrsToShow;
    }

    public static Calendar getCalendar(String date) {
        String[] dayMonthYear = date.split("-");
        int day = Integer.parseInt(dayMonthYear[2].split(" ")[0]);
        int month = Integer.parseInt(dayMonthYear[1]);
        int year = Integer.parseInt(dayMonthYear[0]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }

}

