package ara.com.amazetravels;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import ara.com.amazetravels.ara.com.amazetravels.models.Booking;
import ara.com.amazetravels.ara.com.amazetravels.models.BookingHistoryAdapter;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.getBookingHistoryAction;

public class BookingHistory extends AppCompatActivity {
    private final static String TAG = "Booking History";
    @BindView(R.id.lv_booking_history)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        ButterKnife.bind(this);

        ArrayList<Booking> bookings = new ArrayList<>();


        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(getBookingHistoryAction());

        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put(CUSTOMER_ID, AppConstants.getCurrentUser().getCustomerId() + "");
        httpRequest.setParams(params);
        new HttpCaller(this, "Loading Booking Histrory") {
            @Override
            public void onResponse(HttpResponse response) {
                super.onResponse(response);
                ArrayList<Booking> bookings = getModel(response);

                listView.setAdapter(new BookingHistoryAdapter(BookingHistory.this, bookings));
                listView.invalidate();
            }
        }.execute(httpRequest);

    }

    private ArrayList<Booking> getModel(HttpResponse response) {
        ArrayList<Booking> bookings = new ArrayList<>(15);
        try {
            JSONArray jsonArray = new JSONArray(response.getMesssage());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Calendar calendar = AppConstants.getCalendar(jsonObject.getString("book_date"));
                Booking booking = new Booking(
                        jsonObject.getInt("bookId"),
                        jsonObject.getString("booking_status"),
                        calendar);
                bookings.add(booking);
            }
        } catch (JSONException jsonException) {
            Log.e(TAG, jsonException.getMessage(), jsonException);
        }
        return bookings;
    }
}
