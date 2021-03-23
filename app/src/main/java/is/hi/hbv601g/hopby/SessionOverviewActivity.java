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

    private LocalDate day1 = LocalDate.of(2021, Month.APRIL, 1);
    private LocalDate day2 = LocalDate.of(2021, Month.APRIL, 5);
    private LocalTime time1 = LocalTime.of(12,00);
    private LocalTime time2 = LocalTime.of(14,00);
    private Session[] mSessionBank = new Session[] {
            new Session("Georg og felagar", "Nordakjallarinn", day1, time1, 10, 1, "Rosa gaman, mikid stud"),
            new Session("ELDGOS", "Fagradalsfjall", day1, time2, 10, 3, "Kikjum a thetta gos"),
            new Session("Addi og co", "Klambratun", day2, time1, 10, 2, "Krofuboltastud"),
            new Session("Fallega folkid", "Seljaskoli", day2, time1, 10, 2, "Rosa gaman, mikid stud")
    };

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



        updateSessions();
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
        mSessionTitle.setText(mSessionBank[0].getTitle());
        mSessionDescription = (TextView) findViewById(R.id.Session_description);
        mSessionDescription.setText(mSessionBank[0].getDescription());
        mSessionLocation = (TextView) findViewById(R.id.Session_location);
        mSessionLocation.setText(mSessionBank[0].getLocation());
        mSessionDate = (TextView) findViewById(R.id.Session_date);
        mSessionDate.setText(mSessionBank[0].getDate().toString());
        mSessionTime = (TextView) findViewById(R.id.Session_time);
        mSessionDate.setText(mSessionBank[0].getTime().toString());
        mSessionSlots = (TextView) findViewById(R.id.Session_slots);
        mSessionSlots.setText("Total slots: " + mSessionBank[0].getSlots() + " Available slots: " + mSessionBank[0].getSlotsAvailable());
        mSessionHobby =  (TextView) findViewById(R.id.Session_hobby);
        mSessionHobby.setText(String.valueOf(mSessionBank[0].getHobbyId()));
    }

}