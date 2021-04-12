package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class CreateSessionActivity extends AppCompatActivity {

    private Button mButtonSubmit;
    private Button mButtonCancel;
    private Button mButtonMaps;

    private TextInputEditText mTitle;
    private TextInputEditText mDate;
    private TextInputEditText mTime;
    private TextInputEditText mSlots;
    private TextInputEditText mDescription;
    private TextInputEditText mLocation;
    private TextInputEditText mHobbyId;

    private SessionService mSessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        NetworkController networkController = NetworkController.getInstance(this);

        mSessionService = new SessionService(networkController);

        mButtonSubmit = (Button) findViewById(R.id.submit_button);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle = findViewById(R.id.input_title);
                mDate = findViewById(R.id.input_date);
                mTime = findViewById(R.id.input_time);
                mSlots = findViewById(R.id.input_slots);
                mDescription = findViewById(R.id.input_description);
                mLocation = findViewById(R.id.input_location);
                mHobbyId = findViewById(R.id.input_hobbyId);

                int slots = Integer.parseInt(mSlots.getText().toString());
                int hobby = Integer.parseInt(mHobbyId.getText().toString());
                mSessionService.addSession(mTitle.getText().toString(), mDate.getText().toString(), mTime.getText().toString(), slots, hobby, mDescription.getText().toString(), mLocation.getText().toString());

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
}