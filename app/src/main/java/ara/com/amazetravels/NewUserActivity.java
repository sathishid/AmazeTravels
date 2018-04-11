package ara.com.amazetravels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ara.com.amazetravels.ara.com.amazetravels.models.Customer;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewUserActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_address)
    EditText _addressText;
    @BindView(R.id.input_area)
    EditText _areaText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        ButterKnife.bind(this);



        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signUp(View view) {


        Log.d(TAG, "SignUp");

        if (!validate()) {
            onSignupFailed(null);
            return;
        }

        _signupButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String area = _areaText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        Customer customer = new Customer(1, name, password, mobile, address, area, "Chennai");

        //Sending the new user details to Server.
        try {
            final HttpRequest httpRequest = new HttpRequest();

            httpRequest.setUrl(AppConstants.getAddCustomerUrl());

            httpRequest.setParams(customer.toHashMap());
            httpRequest.setMethodtype(HttpRequest.POST);

            new HttpCaller(NewUserActivity.this,"Creating User...") {

                @Override
                public void onResponse(HttpResponse response) {

                    super.onResponse(response);

                    if (response.getStatus() == HttpResponse.ERROR) {
                        Log.e("Customer Add Error", response.getMesssage());
                        Toast.makeText(NewUserActivity.this, response.getMesssage(), Toast.LENGTH_LONG);
                        onSignupFailed(response);
                    } else {

                        Log.i("Customer Add Success", response.getMesssage());
                        onSignupSuccess(response);
                    }

                }
            }.execute(httpRequest);
        } catch (Exception exception) {
            Toast.makeText(NewUserActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("URL Malfunction :", exception.getMessage());
        }
    }


    public void onSignupSuccess(HttpResponse response) {
        _signupButton.setEnabled(true);

        try {
            String strResponse = response.getMesssage();

            if (strResponse
                    .compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) != 0) {
                Toast.makeText(getBaseContext(), strResponse, Toast.LENGTH_LONG).show();
                return;
            }
            Log.i("Customer Add Success", response.getMesssage());


            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();

        } catch (Exception e) {
            Log.e("On Login Success", e.getMessage());
            Toast.makeText(NewUserActivity.this, "Something Went Wrong,Contact Support", Toast.LENGTH_LONG).show();
        }

    }

    public void onSignupFailed(HttpResponse response) {
        if (response != null) {
            Toast.makeText(NewUserActivity.this, "Something Went Wrong! Please check the network connection.", Toast.LENGTH_SHORT).show();
            Log.e("Creation Failed", response.getMesssage());
            Toast.makeText(getBaseContext(), "New User creation Failed, Contact Support!", Toast.LENGTH_LONG).show();
        }
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();

        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
