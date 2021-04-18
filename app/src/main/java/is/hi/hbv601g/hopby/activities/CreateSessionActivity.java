package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateSessionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mButtonSubmit;
    private Button mButtonCancel;
    private Button mButtonMaps;

    private TextInputEditText mTitle;
    private TextInputEditText mDate;
    private TextInputEditText mTime;
    private TextInputEditText mSlots;
    private TextInputEditText mDescription;
    private TextInputEditText mLocation;
    private String mHobbyId;
    private CalendarView mCalendarView;

    private SessionService mSessionService;
    private String mLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        NetworkController networkController = NetworkController.getInstance(this);
        mSessionService = new SessionService(networkController);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInUser", "");

        final Spinner hobbySpinner = (Spinner) findViewById(R.id.hobby_spinner);
        hobbySpinner.setOnItemSelectedListener(this);

        List<String> hobbies = mSessionService.getHobbies();


        mCalendarView = findViewById(R.id.input_date_calendarView);
        mCalendarView.setMinDate(System.currentTimeMillis());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hobbies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hobbySpinner.setAdapter(dataAdapter);

        mButtonSubmit = (Button) findViewById(R.id.submit_button);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle = findViewById(R.id.input_title);
                mCalendarView = findViewById(R.id.input_date_calendarView);
                mTime = findViewById(R.id.input_time);
                mSlots = findViewById(R.id.input_slots);
                mDescription = findViewById(R.id.input_description);
                mLocation = findViewById(R.id.input_location);

                // Upon illegal input display error message
                boolean isOk = true;
                if(!isLegalLoc(mLocation.getText().toString())) {
                    mLocation.setError("Location not found");
                    isOk = false;
                }
                if(mTime.length() != 4){
                    mTime.setError("Time must be in the format 'hhmm'");
                    isOk = false;
                }
                if(mTitle.length() == 0){
                    mTitle.setError("Required field");
                    isOk = false;
                }
                if(mSlots.length() == 0){
                    mSlots.setError("Required field");
                    isOk = false;
                } else if(Integer.parseInt(mSlots.getText().toString()) < 2) {
                    mSlots.setError("Slots must be greater than 1");
                    isOk = false;
                }

                if(isOk) {
                    // TODO: breyta hér þannig það opni info en ekki overview
                    long resultId = mSessionService.addSession(mLoggedInUser, mTitle, mCalendarView, mTime, mSlots, mHobbyId, mDescription, mLocation, mLoggedInUser);

                    Intent intent = new Intent(CreateSessionActivity.this, SessionOverviewActivity.class);
                    intent.putExtra("id", Long.toString(resultId));
                    startActivity(intent);
                }
            }
        });

        mButtonCancel = (Button) findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean isLegalLoc(String createLocation) {
        // Checks if there is at least one legal address from the input text
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        try {
            address = geocoder.getFromLocationName(createLocation, 1);

            if (address.size() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        mHobbyId = item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mHobbyId = "Football";

    }
}