package is.hi.hbv601g.hopby;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.activities.FilterActivity;
import is.hi.hbv601g.hopby.activities.LoginActivity;
import is.hi.hbv601g.hopby.activities.SignupActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mButtonAll;
    private Button mButtonMy;
    private TextView mGreetings;

    private String mLoggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInName", "");

        mGreetings = (TextView) findViewById(R.id.greetings);
        mGreetings.setText("Hi! " + mLoggedInUser );

        mButtonAll = (Button) findViewById(R.id.button_view_all_sessions);
        mButtonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        mButtonMy = (Button) findViewById(R.id.button_view_my_sessions);
        mButtonMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MySessionsActivity.class);
                startActivity(intent);
                // TODO opna nytt nice
                Log.d("MainActivity", "OPNA NYTT NICE");
            }
        });

    }
}