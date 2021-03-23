package is.hi.hbv601g.hopby;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class SessionOverviewActivity extends AppCompatActivity {

    private Button mButtonFilter;
    private Button mButtonCreate;
    private Button mButtonMaps;
    private TextView mSessionTitle;
    private TextView mSessionDescription;
    private TextView mSessionLocation;
    private TextView mSessionDate;
    private TextView mSessionTime;
    private TextView mSessionHobby;
    private TextView mSessionSlots;

    private List<Session> mSessionBank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_overview);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        NetworkController networkController = NetworkController.getInstance(this);
        networkController.getSessions(new NetworkCallback<List<Session>>() {
            @Override
            public void onSuccess(List<Session> result) {
                mSessionBank = result;
                Log.d("SessionOverviewActivity", "First session in bank "+mSessionBank.get(0).getTitle());
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionOverviewActivity", "Failed to get sessions "+ errorString);
            }
        });

        mButtonFilter = (Button) findViewById(R.id.filter_button);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("filter TAKKI");
            }
        });

        mButtonCreate = (Button) findViewById(R.id.createSession_button);
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
    private void updateSessions() {
        mSessionTitle = (TextView) findViewById(R.id.Session_title);
        mSessionTitle.setText(mSessionBank.get(0).getTitle());
        mSessionDescription = (TextView) findViewById(R.id.Session_description);
        mSessionDescription.setText(mSessionBank.get(0).getDescription());
        mSessionLocation = (TextView) findViewById(R.id.Session_location);
        mSessionLocation.setText(mSessionBank.get(0).getLocation());
        mSessionDate = (TextView) findViewById(R.id.Session_date);
        mSessionDate.setText(mSessionBank.get(0).getDate().toString());
        mSessionTime = (TextView) findViewById(R.id.Session_time);
        mSessionDate.setText(mSessionBank.get(0).getTime().toString());
        mSessionSlots = (TextView) findViewById(R.id.Session_slots);
        mSessionSlots.setText("Total slots: " + mSessionBank.get(0).getSlots() + " Available slots: " + mSessionBank.get(0).getSlotsAvailable());
        mSessionHobby =  (TextView) findViewById(R.id.Session_hobby);
        mSessionHobby.setText(String.valueOf(mSessionBank.get(0).getHobbyId()));
    }
}