package is.hi.hbv601g.hopby.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.hopby.InfoAdapter;
import is.hi.hbv601g.hopby.InfoModel;
import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionOverviewActivity extends AppCompatActivity {

    private Button mButtonFilter;
    private Button mButtonCreate;
    private Button mButtonMaps;
    GridView grid;

    private SessionService mSessionService;
    private List<Session> mSessionBank;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_overview);


        NetworkController networkController = NetworkController.getInstance(this);
        mSessionService = new SessionService(networkController, this);

        mButtonFilter = (Button) findViewById(R.id.submit_button);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("filter TAKKI");
            }
        });

        mButtonCreate = (Button) findViewById(R.id.cancel_button);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("createSession TAKKI");
            }
        });

        mButtonMaps = (Button) findViewById(R.id.maps_button);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                System.out.println("MAPS TAKKI");
                Intent intent = new Intent(SessionOverviewActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
    public void updateSessions(List<Session> mSessionBank) {
        grid = findViewById(R.id.overview_grid);
        ArrayList<Session> sessionArrayList = new ArrayList<Session>();

        int length = mSessionBank.size();
        for (int i = 0; i < length; i++) {
            LocalDate localDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                localDate = LocalDate.parse("2021-04-20");
            }
            LocalTime localTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                localTime = LocalTime.parse("12:00");
            }
            sessionArrayList.add(new Session(mSessionBank.get(i).getTitle(), mSessionBank.get(i).getLocation(), localDate, localTime, mSessionBank.get(i).getSlots(), mSessionBank.get(i).getHobbyId(), mSessionBank.get(i).getDescription()));
            Log.d("SessionOverviewActivity", " " + mSessionBank.get(i).getSlots());
        }
        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList);
        grid.setAdapter(adapter);
    }


}