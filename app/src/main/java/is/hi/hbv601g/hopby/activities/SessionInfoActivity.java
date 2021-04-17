package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.hopby.InfoModel;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.InfoAdapter;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionInfoActivity extends AppCompatActivity {
    private Button mButtonMaps;
    private Button mButtonBack;
    private Button mButtonJoin;
    private List<Session> mSessionBank;

    private long mId;
    private String mLoggedInUser;

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

            }
        });;
    }
    public void updateSession(Session session) {
        grid = findViewById(R.id.info_grid);
        ArrayList<InfoModel> sessionArrayList = new ArrayList<InfoModel>();

        sessionArrayList.add(new InfoModel(session.getTitle(), "Title"));
        sessionArrayList.add(new InfoModel(session.getDescription(), "Description"));
        if(session.getHobbyId() == 1)  sessionArrayList.add(new InfoModel("Football", "HobbyId"));
        if(session.getHobbyId() == 2)  sessionArrayList.add(new InfoModel("Basketball", "HobbyId"));
        if(session.getHobbyId() == 3)  sessionArrayList.add(new InfoModel("Hike", "HobbyId"));
        sessionArrayList.add(new InfoModel(session.getLocation(), "Location"));
        sessionArrayList.add(new InfoModel(String.valueOf(session.getSlotsAvailable())+ " / " + String.valueOf(session.getSlots()), "Slots"));
        sessionArrayList.add(new InfoModel(session.getUsers().toString(), "Users"));

        InfoAdapter adapter = new InfoAdapter(this, sessionArrayList);
        grid.setAdapter(adapter);
    }

}