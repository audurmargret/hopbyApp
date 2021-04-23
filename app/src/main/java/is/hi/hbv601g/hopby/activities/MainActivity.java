package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.channels.Channel;

public class MainActivity extends AppCompatActivity {

    private Button mButtonAll;
    private Button mButtonMy;
    private Button mButtonLogout;
    private TextView mGreetings;

    private String mLoggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get name of logged in user
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInName", "");

        // Greet the user
        mGreetings = (TextView) findViewById(R.id.greetings);
        mGreetings.setText("Hi! " + mLoggedInUser );

        // Button to open all sessions (filter)
        mButtonAll = (Button) findViewById(R.id.button_view_all_sessions);
        mButtonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        // Button to open my sessions
        mButtonMy = (Button) findViewById(R.id.button_view_my_sessions);
        mButtonMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MySessionsActivity.class);
                startActivity(intent);
            }
        });

        // Button to log out
        mButtonLogout = (Button) findViewById(R.id.button_logout);
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}