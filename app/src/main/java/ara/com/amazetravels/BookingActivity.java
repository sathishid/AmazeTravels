package ara.com.amazetravels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ara.com.amazetravels.ara.com.amazetravels.models.VehicleType;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import ara.com.amazetravels.ara.com.utils.VehicleTypeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingActivity extends AppCompatActivity {
    @BindView(R.id.main_scrollView)
    ScrollView scrollView;
    @BindView(R.id.checkbox_other_user)
    CheckBox checkBox_otherUser;
    @BindView(R.id.layout_other_user)
    LinearLayout layout_OtherUser;
    @BindView(R.id.input_other_user)
    EditText input_OtherUser;
    @BindView(R.id.input_other_mobile)
    EditText input_OtherMobile;
    @BindView(R.id.spinner_vehicle_type)
    Spinner spinner_VehicleType;
    @BindView(R.id.btn_booking)
    Button button_booking;

    SpinnerAdapter vehicleTypes_spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);
        populateValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void populateValues() {
        input_OtherUser.setEnabled(false);
        input_OtherMobile.setEnabled(false);
        input_OtherUser.setText(AppConstants.getCurrentUser().getUserName());
        input_OtherMobile.setText(AppConstants.getCurrentUser().getMobileNo());

        initVehicleTypes();
    }

    public void onForFamilyClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if (isChecked) {
            input_OtherUser.setText("");
            input_OtherMobile.setText("");
            input_OtherMobile.setEnabled(true);
            input_OtherUser.setEnabled(true);

        } else {
            input_OtherUser.setText(AppConstants.getCurrentUser().getUserName());
            input_OtherMobile.setText(AppConstants.getCurrentUser().getMobileNo());
            input_OtherMobile.setEnabled(false);
            input_OtherUser.setEnabled(false);

        }

    }

    private void initVehicleTypes() {
        button_booking.setEnabled(false);
        final HttpRequest httpRequest = new HttpRequest();

        httpRequest.setUrl(AppConstants.getVehicleTypeApi());

        new HttpCaller(BookingActivity.this) {
            @Override
            public void onResponse(HttpResponse response) {
                button_booking.setEnabled(true);
                super.onResponse(response);
                if (response.getStatus() == HttpResponse.ERROR) {
                    Log.e("Vehicle Add Issue", response.getMesssage());

                } else {
                    Log.i("Vehicle Add Success", response.getMesssage());
                    updateVehicleTypes(response);

                }
            }
        }.execute(httpRequest);

    }

    private void updateVehicleTypes(HttpResponse response) {
        if (response.getStatus() == HttpResponse.ERROR)
            return;
        try {

            JSONArray vehicleTypes = new JSONArray(response.getMesssage());

            VehicleType[] arrVehicleTypes = new VehicleType[vehicleTypes.length()];
            for (int i = 0; i < vehicleTypes.length(); i++) {
                JSONObject vehicleTypeJsonObject = vehicleTypes.getJSONObject(i);
                int id = vehicleTypeJsonObject.getInt(AppConstants.VEHICLE_TYPE_ID);
                VehicleType vehicleType = new VehicleType(id,
                        vehicleTypeJsonObject.getString(AppConstants.VEHICLE_NAME));
                arrVehicleTypes[i] = vehicleType;
            }
            vehicleTypes_spinnerAdapter = new VehicleTypeAdapter(BookingActivity.this,
                    R.layout.support_simple_spinner_dropdown_item,
                    arrVehicleTypes);
            spinner_VehicleType.setAdapter(vehicleTypes_spinnerAdapter);

            Log.i("Update Vehicle Types", "Vehicle Type Count:" + vehicleTypes.length());
        } catch (Exception e) {
            Log.e("Vechicle Type", e.toString());
        }
    }


    public void bookRide_Clicked(View view) {
        button_booking.setEnabled(false);

        if (!validate()) {
            onBookingFailure(null);
            return;
        }
        String otherUser = "";
        String otherMobile = "";
        if (checkBox_otherUser.isChecked()) {
            otherUser = input_OtherUser.getText().toString();
            otherMobile = input_OtherMobile.getText().toString();
        }
        Booking booking = new Booking(
                1,
                AppConstants.getCurrentUser(),
                otherUser,
                otherMobile,
                ((VehicleType) spinner_VehicleType.getSelectedItem()).getVehicleId());
        try {
            final HttpRequest httpRequest = new HttpRequest();

            httpRequest.setUrl(AppConstants.getBookingApi());

            httpRequest.setParams(booking.toHashMap());
            httpRequest.setMethodtype(HttpRequest.POST);

            final ProgressDialog progressDialog = new ProgressDialog(BookingActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            new HttpCaller(BookingActivity.this) {
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
                    progressDialog.dismiss();
                }
            }.execute(httpRequest);
        } catch (Exception exception) {
            Toast.makeText(BookingActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("URL Malfunction :", exception.getMessage());
        }

    }

    private void onBookingFailure(HttpResponse response) {
        if (response != null) {
            Toast.makeText(BookingActivity.this, "Something Went Wrong! Please check the network connection.", Toast.LENGTH_SHORT).show();
        }
        button_booking.setEnabled(true);
    }

    private void onBookingSuccess(HttpResponse response) {
        button_booking.setEnabled(true);
        if (!response.getMesssage().contains(AppConstants.SUCCESS_MESSAGE)) {
            Toast.makeText(BookingActivity.this, "Something Went Wrong! Please contact support.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (response.getMesssage().compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) == 0) {
            clearAll();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();

        }

    }

    private void clearAll() {
        if (checkBox_otherUser.isChecked()) {
            checkBox_otherUser.setChecked(false);
            input_OtherMobile.setText("");
            input_OtherUser.setText("");
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = input_OtherUser.getText().toString();

        String mobile = input_OtherMobile.getText().toString();


        if (checkBox_otherUser.isChecked() && (name.isEmpty() || name.length() < 3)) {
            input_OtherUser.setError("at least 3 characters");
            valid = false;
        } else {
            input_OtherUser.setError(null);
        }


        if (checkBox_otherUser.isChecked() && (mobile.isEmpty() || mobile.length() != 10)) {
            input_OtherMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            input_OtherMobile.setError(null);
        }


        return valid;
    }
}
