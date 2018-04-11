package ara.com.amazetravels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ara.com.amazetravels.ara.com.amazetravels.models.Booking;
import ara.com.amazetravels.ara.com.amazetravels.models.BookingTypes;
import ara.com.amazetravels.ara.com.amazetravels.models.Customer;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_NAME;
import static ara.com.amazetravels.ara.com.utils.AppConstants.IDENTITY;
import static ara.com.amazetravels.ara.com.utils.AppConstants.MOBILE_NUMBER;
import static ara.com.amazetravels.ara.com.utils.AppConstants.PREFERENCE_NAME;
import static ara.com.amazetravels.ara.com.utils.AppConstants.REQUEST_LOGIN;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.layout_scrollView_root)
    View scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.contains(CUSTOMER_ID)) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
        else{
            updateCurrentUser();
        }
    }

    public void bookRide_OnClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_BOOKING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {

            updateCurrentUser();
        }
        if (requestCode == AppConstants.REQUEST_BOOKING) {
            if (resultCode == RESULT_OK) {
                showSuccessSnackbar();
            }
        } else if (requestCode == AppConstants.REQUEST_VOICE_BOOKING && resultCode == RESULT_OK) {
            showSuccessSnackbar();
        }
    }

    private void updateCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(CUSTOMER_ID, -1);
        String customerName = sharedPreferences.getString(CUSTOMER_NAME, null);
        String mobileNo = sharedPreferences.getString(MOBILE_NUMBER, null);
        Customer customer = new Customer(id, customerName, null, mobileNo, null, null, null);
        AppConstants.setCustomer(customer);
    }

    private void showSuccessSnackbar() {
        final Snackbar snackbar = Snackbar.make(scrollView, R.string.dialog_message_booked,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok_button_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void btn_voice_booking_OnClick(View view) {
        Intent intent = new Intent(this, VoiceBookingActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_VOICE_BOOKING);
    }

    public void btn_emergency_booking_OnClick(View view) {
        emergencyBooking();
    }

    private void emergencyBooking() {
        Booking booking = new Booking(
                AppConstants.getCurrentUser(),
                BookingTypes.EMERGENCY);
        try {
            final HttpRequest httpRequest = new HttpRequest();

            httpRequest.setUrl(AppConstants.getBookingApi());

            httpRequest.setParams(booking.toHashMap());
            httpRequest.setMethodtype(HttpRequest.POST);


            new HttpCaller(MainActivity.this, "Emergency Booking...") {
                @Override
                public void onResponse(HttpResponse response) {

                    super.onResponse(response);

                    if (response.getStatus() == HttpResponse.ERROR) {
                        Log.e("Book Ride Error", response.getMesssage());

                        onBookingFailure(response);
                    } else {
                        Log.i("Booking Ride Success", response.getMesssage());
                        onBookingSuccess(response);
                    }

                }
            }.execute(httpRequest);
        } catch (Exception exception) {
            Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("URL Malfunction :", exception.getMessage());
        }
    }

    private void onBookingFailure(HttpResponse response) {
        if (response != null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong! Please check the network connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private void onBookingSuccess(HttpResponse response) {

        if (!response.getMesssage().contains(AppConstants.SUCCESS_MESSAGE)) {
            Toast.makeText(MainActivity.this, "Something Went Wrong! Please contact support.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (response.getMesssage().compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) == 0) {
            showSuccessSnackbar();
        }

    }
}
