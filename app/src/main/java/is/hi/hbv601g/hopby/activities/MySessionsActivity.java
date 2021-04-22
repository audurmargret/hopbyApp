package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import is.hi.hbv601g.hopby.NotificationHelper;
import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.app.Notification;
import android.app.NotificationManager;
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

import java.nio.channels.InterruptedByTimeoutException;
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

    public void onTimeSet(int hourofDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourofDay);
        c.set(Calendar.MINUTE, minute);
    }

    public void cancelAlarm() {

    }
}