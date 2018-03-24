package ara.com.amazetravels;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {

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
    @BindView(R.id.spinner_tariff)
    Spinner spinner_Tariff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);

        initOnCreate();

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

    private void initOnCreate() {
        populateSpinner();


    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> vehicleAdapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_types, android.R.layout.simple_spinner_item);

        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_VehicleType.setAdapter(vehicleAdapter);

        ArrayAdapter<CharSequence> tariffAdapter = ArrayAdapter.createFromResource(this,
                R.array.tariffs, android.R.layout.simple_spinner_item);

        tariffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_Tariff.setAdapter(tariffAdapter);

    }

    public void onForFamilyClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if (isChecked) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout_OtherUser.setLayoutParams(layoutParams);
            scrollView.requestLayout();
            scrollView.invalidate();

        }
    }
}
