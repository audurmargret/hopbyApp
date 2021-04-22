package is.hi.hbv601g.hopby.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    private TextInputEditText mSlots;
    private TextInputEditText mDescription;
    private TextInputEditText mLocation;
    private Spinner mHobbySpinner;
    private String mHobbyId;
    private long mSessionId;
    private CalendarView mCalendarView;
    private String date;
    private TimePicker mTimePicker;
    private TextView mErrorMessage;

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
        hobbies.add(0,"Select Hobby:");

        mCalendarView = findViewById(R.id.input_date_calendarView);
        mCalendarView.setMinDate(System.currentTimeMillis());

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String month = String.valueOf(i1+1);
                if((i1+1)<10) month = "0".concat(month);
                String day = String.valueOf(i2);
                if(i2<10) day = "0".concat(day);
                date = String.valueOf(i).concat("-").concat(month).concat("-").concat(day);
                Log.d("CreateSessionActivity", date);
            }
        });


        mTimePicker = findViewById(R.id.simpleTimePicker);
        mTimePicker.setIs24HourView(true);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hobbies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hobbySpinner.setAdapter(dataAdapter);

        mButtonSubmit = (Button) findViewById(R.id.submit_button);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle = findViewById(R.id.input_title);
                mCalendarView = findViewById(R.id.input_date_calendarView);
                mSlots = findViewById(R.id.input_slots);
                mDescription = findViewById(R.id.input_description);
                mLocation = findViewById(R.id.input_location);
                mHobbySpinner = findViewById(R.id.hobby_spinner);

                String hourString = String.valueOf(mTimePicker.getHour());
                String minString = String.valueOf(mTimePicker.getMinute());
                String timeString = hourString.concat(minString);
                Log.d("CreateSessionActivity", timeString);


                // Upon illegal input display error message
                boolean isOk = true;
                if(!isLegalLoc(mLocation.getText().toString())) {
                    mLocation.setError("Location not found");
                    isOk = false;
                }
                if(mTitle.length() == 0){
                    mTitle.setError("Required field");
                    isOk = false;
                }
                if(mHobbyId.equals("") || mHobbyId.equals("Select Hobby:")) {
                    TextView errorText = (TextView) mHobbySpinner.getSelectedView();
                    errorText.setError("Required field");
                    Log.d("CreateSessionActivity", "ERRROR Á HOBBYYYY");
                    isOk = false;
                }
                if(mSlots.length() == 0) {
                    mSlots.setError("Required field");
                    isOk = false;
                } else if(Integer.parseInt(mSlots.getText().toString()) < 2) {
                    mSlots.setError("Slots must be greater than 1");
                    isOk = false;
                }


                Log.d("CreateSEssion", "DATE " + mCalendarView.getDate());
                if(isOk) {
                    // TODO: breyta hér þannig það opni info en ekki overview
                    Log.d("CreateSEssionActivity", "LOGGED IN USER: " + mLoggedInUser);
                    long resultId = mSessionService.addSession(mTitle, date, timeString, mSlots, mHobbyId, mDescription, mLocation, mLoggedInUser);

                    Intent intent = new Intent(CreateSessionActivity.this, SessionOverviewActivity.class);
                    intent.putExtra("id", Long.toString(resultId));
                    startActivity(intent);
                }
                else {
                    mErrorMessage = findViewById(R.id.error_message_create);
                    mErrorMessage.setVisibility(View.VISIBLE);
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

        mButtonMaps = (Button) findViewById(R.id.maps_button);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateSessionActivity.this, MapsActivity.class);
                intent.putExtra("flag","create");
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (10) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("Address");
                    TextView locationView = (TextView)findViewById(R.id.input_location);
                    locationView.setText(newText);
                }
                break;
            }
        }
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

    public void setSessionId(long id) {
        mSessionId = id;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d("CreateSessionActivity", "SESSION" + item);
        mHobbyId = item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mHobbyId = "";

    }
}