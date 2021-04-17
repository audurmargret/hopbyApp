package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.hi.hbv601g.hopby.AlertDialogDelete;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionInfoActivity extends AppCompatActivity implements AlertDialogDelete.AlertDialogDeleteListener {
    private Button mButtonMaps;
    private Button mButtonBack;
    private Button mButtonJoin;
    private LinearLayout mLinearLayoutHost;
    private Button mButtonDelete;
    private List<Session> mSessionBank;

    private TextView mTitle;
    private TextView mDescription;
    private TextView mHobbyId;
    private TextView mLocation;
    private TextView mSlots;
    private TextView mUsers;




    private long mId;
    private String mLoggedInUser;
    private boolean mIsInSession;
    private boolean mIsHost;
    private boolean mConfirmDelete;

    private SessionService mSessionService;

    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        NetworkController networkController = NetworkController.getInstance(this);

        mSessionService = new SessionService(networkController);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInUser", "");
        Log.d("SessionOverview", "LOGGED IN USER: " + mLoggedInUser + " ++++++++++");

        Intent getIntent = getIntent();
        mId = Long.parseLong(getIntent.getStringExtra("id"));

        mSessionService.getSession(this, mId);

        mButtonMaps = (Button) findViewById(R.id.info_button_maps);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                System.out.println("MAPS TAKKI");
                Intent intent = new Intent(SessionInfoActivity.this, MapsActivity.class);
                intent.putExtra("flag","info");
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        mButtonBack = (Button) findViewById(R.id.info_button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        mButtonJoin = (Button) findViewById(R.id.info_button_join);
        mButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Breyta yfir í leave ef viðkomandi er þegar skráður
                // TODO: Birta toast ef það tókst að join-a
                System.out.println(mLoggedInUser + " is trying to join");
                if(mIsInSession) {
                    Log.d("SessionInfoActivity", "USER IS IN SESSION");
                    mSessionService.joinSession(mId, mLoggedInUser, "leaveSession");
                    finish();
                    startActivity(getIntent());
                } else {
                    mSessionService.joinSession(mId, mLoggedInUser, "joinSession");
                    finish();
                    startActivity(getIntent());
                }
            }
        });;
    }
    public void updateSession(Session session) {

        mTitle = (TextView) findViewById(R.id.info_title);
        mTitle.setText(session.getTitle());

        mDescription = (TextView) findViewById(R.id.info_description);
        mDescription.setText(session.getDescription());

        mHobbyId = (TextView) findViewById(R.id.info_hobbyId);
        if(session.getHobbyId() == 1) mHobbyId.setText("Football");
        else if(session.getHobbyId() == 2) mHobbyId.setText("Basketball");
        else if(session.getHobbyId() == 3) mHobbyId.setText("Hike");

        mLocation = (TextView) findViewById(R.id.info_location);
        mLocation.setText(session.getLocation());

        mSlots = (TextView) findViewById(R.id.info_slots);
        //mSlots.setText(String.valueOf(session.getSlotsAvailable())+ " / " + String.valueOf(session.getSlots()));
        mSlots.setText(String.valueOf(((session.getSlots())-(session.getSlotsAvailable())))+ " / " + String.valueOf(session.getSlots()));

        String userList = mSessionService.getUserList(session);
        mUsers = (TextView) findViewById(R.id.info_users);
        mUsers.setText(userList);



        mIsInSession = mSessionService.isUserInSession(session, mLoggedInUser);
        Log.d("SessionInfoActivity", "mIsInSession " + mIsInSession);
        if(mIsInSession) {
            mIsHost = mSessionService.isUserHost(session, mLoggedInUser);
            if(mIsHost){
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
            }
            mButtonJoin.setText("LEAVE");
        } else if(session.getSlotsAvailable() == 0) {
            mButtonJoin.setEnabled(false);
        }
    }

    @Override
    public void onYesClicked() {
        mConfirmDelete = true;
        Context context = getApplicationContext();
        CharSequence text = "YES DELETE";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mSessionService.deleteSession(mId);
        finish();
    }

}