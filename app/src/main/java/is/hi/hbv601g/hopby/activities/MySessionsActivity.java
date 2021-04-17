package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MySessionsActivity extends AppCompatActivity {

    private GridView grid;
    private TextView mHeader;
    private Button mButtonBack;
    private SessionService mSessionService;
    private String mLoggedInUser;
    private String mLoggedInName;

    private static ArrayList <Session> sessionArrayList; // TODO make not static and pass though intent ??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);

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

    }

    public void updateSessions(List<Session> mSessionBank) {


        if(mSessionBank.size() < 1) {
            // TODO:  birta einhver skilaboð um að þú ´sert ekki skráður í neitt session
            Log.d("MySessionsActivity", "-------ARRAY TÓMT --------");
        }

        sessionArrayList = new ArrayList<Session>(mSessionBank);

        OverviewAdapter adapter = new OverviewAdapter(this, sessionArrayList);
        grid.setAdapter(adapter);
    }
}