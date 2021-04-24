package is.hi.hbv601g.hopby.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import is.hi.hbv601g.hopby.AlertReceiver;
import is.hi.hbv601g.hopby.NotificationHelper;
import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;


public class MySessionsActivity extends AppCompatActivity {

    private GridView grid;
    private TextView mHeader;
    private TextView mNoSessions;
    private Button mButtonBack;
    private SessionService mSessionService;
    private String mLoggedInUser;
    private String mLoggedInName;
    ImageButton notifyButton;

    private NotificationHelper mNotificationHelper;
    private NotificationManagerCompat notificationManager;

    private static ArrayList <Session> sessionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);
        start();
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    public void start() {
        // Connect the Service with the Notification Helper
        mNotificationHelper = new NotificationHelper(this);
        notificationManager = NotificationManagerCompat.from(this);

        // Connect the Service with the Network Controller
        NetworkController networkController = NetworkController.getInstance(this);
        mSessionService = new SessionService(networkController);

        grid = findViewById(R.id.my_overview_grid);

        // Get the logged in user
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInName = preferences.getString("loggedInName", "");
        mLoggedInUser = preferences.getString("loggedInUser", "");

        // Get sessions that the user is attending
        mSessionService.getMySession(this, mLoggedInUser);

        // Set the title
        mHeader = (TextView) findViewById(R.id.my_overview_header);
        mHeader.setText(mLoggedInName.concat("'s Sessions"));

        // Button back to MainActivity
        mButtonBack = (Button) findViewById(R.id.back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Button to open notification in settings on phone
        notifyButton = findViewById(R.id.notification_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                if(Build.VERSION.SDK_INT >= 5 && Build.VERSION.SDK_INT < 8) {
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                    startActivity(intent);
                } else if (Build.VERSION.SDK_INT >= 8) {
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                    startActivity(intent);
                } else {
                    Log.d("MySessionsActivity", "Gat ekki opnad settings");
                }
            }
        });
    }

    public void updateSessions(List<Session> mSessionBank) {
        if(mSessionBank.size() < 1) {
            // If there are no sessions
            mNoSessions = findViewById(R.id.no_sessions);
            mNoSessions.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }

        // Update the grid
        sessionArrayList = new ArrayList<Session>(mSessionBank);
        boolean fromMySessions = true;
        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList, fromMySessions, MySessionsActivity.this);
        grid.setAdapter(adapter);
    }


    // Function for creating a calendar object
    public void onTimeSet(int month, int dayofMonth, int hourofDay, int minute, String title, String message, int id) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayofMonth);
        c.set(Calendar.HOUR_OF_DAY, hourofDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        startAlarm(c, title, message, id);
    }

    // Function for creating a pending intent for Alarm manager
    private void startAlarm(Calendar c, String title, String message, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", title );
        intent.putExtra("message", message);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    // Cancel an alarm in Alarm manager
    public void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    // Create a shared preference variable for Notification on/off per session
    public void setNotificationPref(String id, boolean bool) {
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(id, bool);
        editor.commit();
    }

    // Get boolean value for notification of a session
    public boolean getNotificationPref(String id) {
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        return preferences.getBoolean(id, false);
    }
}