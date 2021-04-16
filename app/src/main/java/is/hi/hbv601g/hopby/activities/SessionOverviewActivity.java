package is.hi.hbv601g.hopby.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionOverviewActivity extends AppCompatActivity {

    private Button mButtonFilter;
    private Button mButtonCreate;
    private Button mButtonMaps;
    private Button mButtonBack;
    private Button mButtonInfo;
    private TextView mFilterText;
    GridView grid;

    private SessionService mSessionService;
    private List<Session> mSessionBank;
    private static ArrayList<Session> sessionArrayList;

    private boolean[] mHobbies;
    private boolean[] mTimes;
    private boolean[] mDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_overview);

        NetworkController networkController = NetworkController.getInstance(this);

        grid = findViewById(R.id.overview_grid);

        Intent getIntent = getIntent();
        boolean filter = getIntent.getBooleanExtra("filter", false);
        mHobbies = getIntent.getBooleanArrayExtra("hobbies");
        mTimes = getIntent.getBooleanArrayExtra("times");
        mDays = getIntent.getBooleanArrayExtra("days");

        mSessionService = new SessionService(networkController);
        mSessionService.getAllSession(this, filter);


        mButtonFilter = (Button) findViewById(R.id.filter_button);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SessionOverviewActivity.this, FilterActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        mButtonCreate = (Button) findViewById(R.id.create_button);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionOverviewActivity.this, CreateSessionActivity.class);
                startActivity(intent);
            }
        });

        mButtonMaps = (Button) findViewById(R.id.maps_button);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                Intent intent = new Intent(SessionOverviewActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        mButtonBack = (Button) findViewById(R.id.logout_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionOverviewActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
    public void updateSessions(List<Session> mSessionBank, boolean filter) {

        if(filter) {
            try{
                sessionArrayList = mSessionService.filter(mSessionBank, mHobbies, mTimes, mDays);
                mFilterText = (TextView) findViewById(R.id.overview_filterText);
                String filterText = mSessionService.filterText(mHobbies, mTimes, mDays);
                mFilterText.setText(filterText);

            } catch (Exception e) {
                Log.d("SessionOverviewActivity", "Gat ekki notað filter" + e.toString());
                sessionArrayList = new ArrayList<Session>();
                mFilterText.setText("Could not load filter");

                int length = mSessionBank.size();
                for (int i = 0; i < length; i++) {
                    sessionArrayList.add(new Session(mSessionBank.get(i).getId(), mSessionBank.get(i).getTitle(), mSessionBank.get(i).getLocation(), mSessionBank.get(i).getDate(), mSessionBank.get(i).getTime(), mSessionBank.get(i).getSlots(), mSessionBank.get(i).getHobbyId(), mSessionBank.get(i).getDescription()));
                }
            }
        } else {
            sessionArrayList = new ArrayList<Session>();

            int length = mSessionBank.size();
            for (int i = 0; i < length; i++) {
                sessionArrayList.add(new Session(mSessionBank.get(i).getId(), mSessionBank.get(i).getTitle(), mSessionBank.get(i).getLocation(), mSessionBank.get(i).getDate(), mSessionBank.get(i).getTime(), mSessionBank.get(i).getSlots(), mSessionBank.get(i).getHobbyId(), mSessionBank.get(i).getDescription()));
            }
        }

        if(sessionArrayList.size() < 1) {
            // TODO:  birta einhver skilaboð um að það hafi ekki uppfylt þessi skilyrði
            Log.d("SessionOverviewActivity", "-------ARRAY TÓMT --------");
        }
        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList);
        grid.setAdapter(adapter);
    }

    public static ArrayList<Session> getSessionArrayList() {
        return sessionArrayList;
    }
}
