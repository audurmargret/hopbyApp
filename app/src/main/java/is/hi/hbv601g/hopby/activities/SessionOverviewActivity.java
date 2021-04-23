package is.hi.hbv601g.hopby.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionOverviewActivity extends AppCompatActivity implements Serializable {

    private Button mButtonFilter;
    private Button mButtonCreate;
    private Button mButtonMaps;
    private Button mButtonBack;
    private TextView mFilterText;
    private TextView mNoSessions;
    private ImageButton mButtonHome;
    private GridView grid;

    private SessionService mSessionService;
    private List<Session> mSessionBank;
    private static ArrayList<Session> sessionArrayList; // TODO make not static and pass through intent

    private boolean[] mHobbies;
    private boolean[] mTimes;
    private boolean[] mDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_overview);
        start();
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    public void start() {
        // Set the grid
        grid = findViewById(R.id.overview_grid);

        // Get the filters
        Intent getIntent = getIntent();
        boolean filter = getIntent.getBooleanExtra("filter", false);
        mHobbies = getIntent.getBooleanArrayExtra("hobbies");
        mTimes = getIntent.getBooleanArrayExtra("times");
        mDays = getIntent.getBooleanArrayExtra("days");

        // Connect to service
        NetworkController networkController = NetworkController.getInstance(this);
        mSessionService = new SessionService(networkController);
        mSessionService.getAllSession(this, filter);

        // Filter button
        mButtonFilter = (Button) findViewById(R.id.filter_button);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Create session button
        mButtonCreate = (Button) findViewById(R.id.create_button);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionOverviewActivity.this, CreateSessionActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        // Maps button
        mButtonMaps = (Button) findViewById(R.id.maps_button);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionOverviewActivity.this, MapsActivity.class);
                intent.putExtra("flag","overview");
                startActivity(intent);
            }
        });

        // Back button to HOME
        mButtonBack = (Button) findViewById(R.id.back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SessionOverviewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        /* Home button - back to MainActivityt
        mButtonHome = (ImageButton) findViewById(R.id.home_button);
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SessionOverviewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/
    }

    // Update the sessions in grid
    public void updateSessions(List<Session> mSessionBank, boolean filter) {
        if(filter) {
            // If there is filter
            try{
                sessionArrayList = mSessionService.filter(mSessionBank, mHobbies, mTimes, mDays);
                mFilterText = (TextView) findViewById(R.id.overview_filterText);
                String filterText = mSessionService.filterText(mHobbies, mTimes, mDays);
                mFilterText.setText(filterText);

            } catch (Exception e) {
                // If something went wrong, get all sessions
                Log.d("SessionOverviewActivity", "Could not use filter " + e.toString());
                sessionArrayList = new ArrayList<Session>();
                mFilterText.setText("Could not load filter");

                sessionArrayList = new ArrayList<Session>(mSessionBank);
            }
        } else {
            // No filter - get all sessions
            sessionArrayList = new ArrayList<Session>(mSessionBank);
        }

        if(sessionArrayList.size() < 1) {
            // If there are no sessions
            mNoSessions = findViewById(R.id.no_sessions_found);
            mNoSessions.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }

        // Set every item in grid with adapter
        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList, false, null );
        grid.setAdapter(adapter);
    }

    // TODO pass through intent not static method
    public static ArrayList<Session> getSessionArrayList() {
        return sessionArrayList;
    }
}
