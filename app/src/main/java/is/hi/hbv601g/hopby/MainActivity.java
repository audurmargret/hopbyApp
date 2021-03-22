package is.hi.hbv601g.hopby;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.ui.login.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class MainActivity extends AppCompatActivity {

    // Upphafstilla adur en tenging kemur vid bakenda

    private Button mButtonLogin;
    private Button mButtonSession;

    private Hobby[] mHobbyBank = new Hobby[] {
            new Hobby("Fotbolti", 1),
            new Hobby("Korfubolti", 2),
            new Hobby("Hike", 3)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonLogin = (Button) findViewById(R.id.login_button);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna LOGIN VIEW
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mButtonSession = (Button) findViewById(R.id.session_button);
        mButtonSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna LOGIN VIEW
                Intent intent = new Intent(MainActivity.this, SessionOverviewActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

}