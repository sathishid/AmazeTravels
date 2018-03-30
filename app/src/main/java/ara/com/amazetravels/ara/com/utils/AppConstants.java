package ara.com.amazetravels.ara.com.utils;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import ara.com.amazetravels.MainActivity;
import ara.com.amazetravels.NewUserActivity;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;

/**
 * Created by sathi on 28-03-2018.
 */

public class AppConstants {
    public static final int SEND_OBJECT = 1;
    public static final int SEND_PARAM = 2;

    public static final String URL_FEED = "http://arasoftwares.in/atnc-app/android_atncfile.php?action=";
    public static final String ADD_CUSTOMER_API = "customersignup";

    public static final String USER_VALIDATE_API="customerlogin";
    private static final String VEHICLE_TYPE_API="vehiclelist";

    public static String getAddCustomerUrl() {
        return URL_FEED + ADD_CUSTOMER_API;
    }
    public static String getUserValidateUrl() {
        return URL_FEED + USER_VALIDATE_API;
    }
    public static String getVehicleTypeApi(){return URL_FEED+VEHICLE_TYPE_API;}

    private static HashMap<Integer,String> vehicleTypes;

    private static void initVehicleTypes() {
            final HttpRequest httpRequest = new HttpRequest();

            httpRequest.setUrl(AppConstants.getVehicleTypeApi());

            new HttpCaller() {
                @Override
                public void onResponse(HttpResponse response) {
                    super.onResponse(response);
                    if (response.getStatus() == HttpResponse.ERROR) {
                        Log.e("Customer Add Error", response.getMesssage());

                    } else {
                        Log.i("Customer Add Success", response.getMesssage());
                        updateVehicleTypes(response);
                    }

                }
            }.execute(httpRequest);
    }
    private static void updateVehicleTypes(HttpResponse response){
        if(response.getStatus()==HttpResponse.ERROR)
            return;
        try {

            JSONObject jsonObject = new JSONObject(response.getMesssage());
            Log.i("json",jsonObject.toString());

        }
        catch(Exception e){
            Log.e("Vechicle Type",e.toString());
        }



    }
    private static String getVechicleType(int id){
        if(vehicleTypes==null){
            initVehicleTypes();
        }
        return vehicleTypes.get(new Integer(id));
    }
}

