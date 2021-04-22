package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import is.hi.hbv601g.hopby.AlertReceiver;
import is.hi.hbv601g.hopby.NotificationHelper;
import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MySessionsActivity extends AppCompatActivity {

    private GridView grid;
    private TextView mHeader;
    private Button mButtonBack;
    private SessionService mSessionService;
    private String mLoggedInUser;
    private String mLoggedInName;
    ImageButton notifyButton;

    private NotificationHelper mNotificationHelper;
    private NotificationManagerCompat notificationManager;

    private static ArrayList <Session> sessionArrayList; // TODO make not static and pass though intent ??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);

        mNotificationHelper = new NotificationHelper(this);
        notificationManager = NotificationManagerCompat.from(this);


        start();
    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d("MySessionsActivity", "ON RESUME!!!!" );
        start();
    }


    public void start() {

        NetworkController networkController = NetworkController.getInstance(this);
        grid = findViewById(R.id.my_overview_grid);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInName = preferences.getString("loggedInName", "");
        mLoggedInUser = preferences.getString("loggedInUser", "");

        mSessionService = new SessionService(networkController);
        mSessionService.getMySession(this, mLoggedInUser);

        mHeader = (TextView) findViewById(R.id.my_overview_header);
        mHeader.setText(mLoggedInName.concat("'s Sessions"));

        mButtonBack = (Button) findViewById(R.id.back_button);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        notifyButton = findViewById(R.id.notification_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                if(Build.VERSION.SDK_INT >= 5 && Build.VERSION.SDK_INT < 8) {
                    Log.d("MySessionsActivity", "fyrri IF");
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                    startActivity(intent);
                } else if (Build.VERSION.SDK_INT >= 8) {
                    Log.d("MySessionsActivity", "seinni IF");
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
            // TODO:  birta einhver skilaboð um að þú ´sert ekki skráður í neitt session
            Log.d("MySessionsActivity", "-------ARRAY TÓMT --------");
        }

        sessionArrayList = new ArrayList<Session>(mSessionBank);

        boolean fromMySessions = true;
        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList, fromMySessions, MySessionsActivity.this);
        grid.setAdapter(adapter);
    }

    public void sendOnChannel(String title, String message, long id) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title, message);
        mNotificationHelper.getManager().notify((int) id, nb.build());
    }


    public void onTimeSet(int month, int dayofMonth, int hourofDay, int minute, String title, String message, int id) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayofMonth);
        c.set(Calendar.HOUR_OF_DAY, hourofDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        startAlarm(c, title, message, id);
    }

    private void startAlarm(Calendar c, String title, String message, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", title );
        intent.putExtra("message", message);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public void setNotificationPref(String id, boolean bool) {
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(id, bool);
        editor.commit();
    }

    public boolean getNotificationPref(String id) {
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        return preferences.getBoolean(id, false);
    }
}