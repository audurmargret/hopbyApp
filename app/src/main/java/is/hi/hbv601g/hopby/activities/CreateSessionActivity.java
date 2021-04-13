package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    private int mHobbyId;
    private CalendarView mCalendarView;

    private SessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        NetworkController networkController = NetworkController.getInstance(this);

        mSessionService = new SessionService(networkController);

        final Spinner hobbySpinner = (Spinner) findViewById(R.id.hobby_spinner);
        hobbySpinner.setOnItemSelectedListener(this);

        List<String> hobbies = new ArrayList<String>();
        hobbies.add("Football");
        hobbies.add("Basketball");
        hobbies.add("Hike");

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

                int slots = Integer.parseInt(mSlots.getText().toString());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = simpleDateFormat.format(mCalendarView.getDate());
                mSessionService.addSession(mTitle.getText().toString(), dateString, mTime.getText().toString(), slots, mHobbyId, mDescription.getText().toString(), mLocation.getText().toString());

                // TODO: breyta hér þannig það opni info en ekki overview
                Intent intent = new Intent(CreateSessionActivity.this, SessionOverviewActivity.class);
                startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d("CreateSessionActivity", "ON CLICK ITEM DROP DROWN + item: " + item);
        if(item.equals("Football")) mHobbyId = 1;
        else if(item.equals("Basketball")) mHobbyId = 2;
        else if(item.equals("Hike")) mHobbyId = 3;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mHobbyId = 1;

    }
}