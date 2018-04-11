package ara.com.amazetravels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import ara.com.amazetravels.ara.com.amazetravels.models.Customer;
import ara.com.amazetravels.ara.com.amazetravels.models.VehicleType;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpCaller;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpRequest;
import ara.com.amazetravels.ara.com.amazetravles.http.HttpResponse;
import ara.com.amazetravels.ara.com.utils.AppConstants;
import ara.com.amazetravels.ara.com.utils.VehicleTypeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_ID;
import static ara.com.amazetravels.ara.com.utils.AppConstants.CUSTOMER_NAME;
import static ara.com.amazetravels.ara.com.utils.AppConstants.MOBILE_NUMBER;
import static ara.com.amazetravels.ara.com.utils.AppConstants.PREFERENCE_NAME;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.layout_linear_root)
    View rootLayout;

    @BindView(R.id.input_login_mobile)
    EditText _login_mobile;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(null);
            return;
        }

        _loginButton.setEnabled(false);


        String mobile = _login_mobile.getText().toString();
        String password = _passwordText.getText().toString();

        //Sending the new user details to Server.
        try {
            final HttpRequest httpRequest = new HttpRequest();

            httpRequest.setUrl(AppConstants.getUserValidateUrl());

            httpRequest.getParams().put("mobileno", mobile);
            httpRequest.getParams().put("password", password);
            httpRequest.setMethodtype(HttpRequest.POST);

            new HttpCaller(LoginActivity.this, "Validating User...") {


                @Override
                public void onResponse(HttpResponse response) {

                    super.onResponse(response);

                    if (response.getStatus() == HttpResponse.ERROR) {
                        onLoginFailed(response);
                    } else {

                        onLoginSuccess(response);
                    }

                }
            }.execute(httpRequest);
        } catch (Exception exception) {
            Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("URL Malfunction :", exception.getMessage());
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Snackbar snackbar = Snackbar.make(rootLayout, "User Created Successfully", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(HttpResponse response) {
        try {
            JSONArray jsonArray = new JSONArray(response.getMesssage());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            _loginButton.setEnabled(true);
            if (jsonObject.getString(AppConstants.LOGIN_RESULT)
                    .compareToIgnoreCase(AppConstants.SUCCESS_MESSAGE) != 0) {
                Toast.makeText(getBaseContext(), "Invalid Mobile or Password!", Toast.LENGTH_LONG).show();
                return;
            }
            Log.i("LoginSuccess", response.getMesssage());

            AppConstants.setCustomer(jsonObject);
            Customer customer = AppConstants.getCurrentUser();
            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CUSTOMER_ID, customer.getCustomerId());
            editor.putString(CUSTOMER_NAME, customer.getName());
            editor.putString(MOBILE_NUMBER, customer.getMobile());
            editor.commit();
            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            Log.e("On Login Success", e.getMessage());
            Toast.makeText(LoginActivity.this, "Something Went Wrong,Contact Support", Toast.LENGTH_LONG);
        }
    }

    public void onLoginFailed(HttpResponse response) {
        _loginButton.setEnabled(true);
        if (response != null) {
            Toast.makeText(LoginActivity.this, response.getMesssage(), Toast.LENGTH_SHORT).show();
            Log.e("Customer Login Failed", response.getMesssage());
        }

    }

    public boolean validate() {
        boolean valid = true;

        String mobile = _login_mobile.getText().toString();
        String password = _passwordText.getText().toString();

        if (mobile.isEmpty() || mobile.length() != 10) {
            _login_mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _login_mobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
