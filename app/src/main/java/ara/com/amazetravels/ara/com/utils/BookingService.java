package ara.com.amazetravels.ara.com.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;

import ara.com.amazetravels.R;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;

import static ara.com.amazetravels.ara.com.utils.AppConstants.PARAM_BOOKING_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.PARAM_STATUS;
import static ara.com.amazetravels.ara.com.utils.AppConstants.SERVICE_PARAM_BOOKING_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.getConfirmedAction;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BookingService extends IntentService {
    private static final String TAG = "Booking Service";

    public BookingService() {
        super("BookingService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startService(Context context, int bookingId) {
        Intent intent = new Intent(context, BookingService.class);
        intent.putExtra(SERVICE_PARAM_BOOKING_ID, bookingId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int booking_id = intent.getIntExtra(SERVICE_PARAM_BOOKING_ID, -1);
            getFromServer(booking_id);
        }
    }

    private void getFromServer(final int bookingId) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(getConfirmedAction());
        HashMap<String, String> params = new HashMap<>(1);
        params.put(PARAM_BOOKING_ID, bookingId + "");
        httpRequest.setParams(params);
        new HttpCaller(this.getApplicationContext(), null) {
            @Override
            public void onResponse(HttpResponse response) {
                super.onResponse(response);
                try {
                    boolean hasConfirmed = response.getJSONObject().getBoolean(PARAM_STATUS);
                    if (hasConfirmed) {
                        showNotification(bookingId, "Your booking " + bookingId + " has been confirmed and" +
                                "cab will arrive before 15 min of your booking time.");
                    } else {
                        startService(getApplicationContext(), bookingId);
                    }
                } catch (JSONException exception) {
                    Log.e(TAG, exception.getMessage(), exception);
                }
            }
        }.execute(httpRequest);
    }

    private void showNotification(int notificationId, String textContent) {
        String textTitle = getResources().getString(R.string.app_name);
        String CHANNEL_ID = "channel_1";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());

    }

}
