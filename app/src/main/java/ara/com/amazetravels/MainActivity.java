package ara.com.amazetravels;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import ara.com.amazetravels.ara.com.amazetravels.models.Booking;
import ara.com.amazetravels.ara.com.amazetravels.models.User;
import ara.com.amazetravels.ara.com.amazetravels.models.VehicleType;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import ara.com.amazetravels.ara.com.utils.VehicleTypeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.layout_scrollView_root)
    View scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    public void bookRide_OnClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivityForResult(intent, AppConstants.REQUEST_BOOKING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_BOOKING) {
            if (resultCode == RESULT_OK) {
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
        }
    }
}
