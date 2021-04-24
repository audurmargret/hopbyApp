package is.hi.hbv601g.hopby.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import is.hi.hbv601g.hopby.AlertDialogDelete;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;
import is.hi.hbv601g.hopby.services.UserService;

public class SessionInfoActivity extends AppCompatActivity implements AlertDialogDelete.AlertDialogDeleteListener {
    private Button mButtonMaps;
    private Button mButtonBack;
    private Button mButtonJoin;
    private LinearLayout mLinearLayoutHost;
    private Button mButtonDelete;
    private List<Session> mSessionBank;

    private TextView mTitle;
    private TextView mDescription;
    private TextView mDate;
    private TextView mTime;
    private TextView mHobbyId;
    private TextView mLocation;
    private TextView mSlots;
    private TextView mHost;
    private TextView mUsers;

    private long mId;
    private String mLoggedInUser;
    private String mLoggedInName;
    private boolean mIsInSession;
    private boolean mIsHost;
    private boolean mConfirmDelete;

    private SessionService mSessionService;
    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        // Set the services
        NetworkController networkController = NetworkController.getInstance(this);
        mSessionService = new SessionService(networkController);
        mUserService = new UserService(networkController);
        mUserService.setUserBank();

        // Get the logged in user
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInUser", "");

        // Get the session
        Intent getIntent = getIntent();
        mId = Long.parseLong(getIntent.getStringExtra("id"));
        mSessionService.getSession(this, mId);

        // Button to view session on maps
        mButtonMaps = (Button) findViewById(R.id.info_button_maps);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionInfoActivity.this, MapsActivity.class);
                intent.putExtra("flag","info");
                startActivity(intent);
            }
        });

        // Button to go back
        mButtonBack = (Button) findViewById(R.id.info_button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Button to join / leave session
        mButtonJoin = (Button) findViewById(R.id.info_button_join);
        mButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "";
                if(mIsInSession) {
                    // user is in session - button is leave
                    mSessionService.joinSession(mId, mLoggedInUser, "leaveSession");
                    text = "Sad to watch you leave!";
                } else {
                    // user is not in session - button is join
                    mSessionService.joinSession(mId, mLoggedInUser, "joinSession");
                    text = "Yay, can't wait to see you!";
                }
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
                startActivity(getIntent());
            }
        });;
    }
    public void updateSession(Session session) {

        // Set the title
        mTitle = (TextView) findViewById(R.id.info_title);
        mTitle.setText(session.getTitle());

        // Set the date
        String date = mSessionService.formatDateStringforView(session.getDate());
        mDate = (TextView) findViewById(R.id.info_date);
        mDate.setText(date);

        // Set the time
        mTime = (TextView) findViewById(R.id.info_time);
        mTime.setText(session.getTime().substring(0,5));

        // Set the description
        mDescription = (TextView) findViewById(R.id.info_description);
        mDescription.setText(session.getDescription());

        // Set the hobby
        mHobbyId = (TextView) findViewById(R.id.info_hobbyId);
        if(session.getHobbyId() == 1) mHobbyId.setText("Football");
        else if(session.getHobbyId() == 2) mHobbyId.setText("Basketball");
        else if(session.getHobbyId() == 3) mHobbyId.setText("Hike");

        // Set the location
        mLocation = (TextView) findViewById(R.id.info_location);
        mLocation.setText(session.getLocation());

        // Set the slots
        mSlots = (TextView) findViewById(R.id.info_slots);
        //mSlots.setText(String.valueOf(session.getSlotsAvailable())+ " / " + String.valueOf(session.getSlots()));
        mSlots.setText(String.valueOf(((session.getSlots())-(session.getSlotsAvailable())))+ " / " + String.valueOf(session.getSlots()));

        // Set the host
        mHost = (TextView) findViewById(R.id.info_host);
        String hostUser = session.getHost();
        mHost.setText(mUserService.getNameForUser(hostUser));

        // Set the list of users
        String userList = mSessionService.getUserList(session);
        mUsers = (TextView) findViewById(R.id.info_users);
        mUsers.setText(userList);

        // If the logged in user is in session
        mIsInSession = mSessionService.isUserInSession(session, mLoggedInUser);
        if(mIsInSession) {
            if(session.getHost().equals(mLoggedInUser)) {
                // If logged in user is the host: enable delete button
                mLinearLayoutHost = (LinearLayout) findViewById(R.id.host);
                mLinearLayoutHost.setVisibility(LinearLayout.VISIBLE);
                mButtonDelete = (Button) findViewById(R.id.info_button_delete);
                mButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialogDelete dialog = new AlertDialogDelete();
                        dialog.show(getSupportFragmentManager(), "Delete dialog");
                    }
                });
                mButtonJoin.setEnabled(false);
            }
            // Change button text to leave
            mButtonJoin.setText("LEAVE");
        } else if(session.getSlotsAvailable() == 0) {
            // If there is no available slots, disable join button
            mButtonJoin.setEnabled(false);
        }
    }

    @Override
    public void onYesClicked() {
        // If delete action was confirmed
        mConfirmDelete = true;
        Context context = getApplicationContext();
        CharSequence text = "Session deleted";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mSessionService.deleteSession(mId);
        finish();
    }

}