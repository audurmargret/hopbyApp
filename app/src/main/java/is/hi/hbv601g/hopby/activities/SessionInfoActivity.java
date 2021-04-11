package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.hopby.InfoModel;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.InfoAdapter;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.SessionService;

public class SessionInfoActivity extends AppCompatActivity {
    private Button mButtonMaps;
    private List<Session> mSessionBank;

    private SessionService mSessionService;
    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        NetworkController networkController = NetworkController.getInstance(this);

        mSessionService = new SessionService(networkController);
        // TODO: breyta 0 yfir í einhvern index til að birta rétt session
        mSessionService.getSession(this, 0);


        mButtonMaps = (Button) findViewById(R.id.info_button_maps);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                System.out.println("MAPS TAKKI");
                Intent intent = new Intent(SessionInfoActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
    public void updateSession(Session session) {
        grid = findViewById(R.id.info_grid);
        ArrayList<InfoModel> sessionArrayList = new ArrayList<InfoModel>();

        sessionArrayList.add(new InfoModel(session.getTitle(), "Title"));
        sessionArrayList.add(new InfoModel(session.getDescription(), "Description"));
        sessionArrayList.add(new InfoModel(session.getLocation(), "Location"));
        sessionArrayList.add(new InfoModel(String.valueOf(session.getSlots()), "Slots"));
        sessionArrayList.add(new InfoModel(session.getUsers().toString(), "Users"));

        InfoAdapter adapter = new InfoAdapter(this, sessionArrayList);
        grid.setAdapter(adapter);
    }

}