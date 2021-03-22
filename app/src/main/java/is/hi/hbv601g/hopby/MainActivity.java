package is.hi.hbv601g.hopby;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class MainActivity extends AppCompatActivity {

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

    // Upphafstilla adur en tenging kemur vid bakenda
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

    private Hobby[] mHobbyBank = new Hobby[] {
            new Hobby("Fotbolti", 1),
            new Hobby("Korfubolti", 2),
            new Hobby("Hike", 3)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateSessions();

        mButtonFilter = (Button) findViewById(R.id.filter_button);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna FILTER VIEW
            }
        });

        mButtonCreate = (Button) findViewById(R.id.createSession_button);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna CREATE VIEW
            }
        });

        mButtonMaps = (Button) findViewById(R.id.maps_button);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                System.out.println("MAPS");
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