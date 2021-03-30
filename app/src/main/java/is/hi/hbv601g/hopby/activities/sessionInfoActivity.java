package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import is.hi.hbv601g.hopby.InfoModel;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.Session;

public class sessionInfoActivity extends AppCompatActivity {
    private Button mButtonMaps;

    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);
        grid = findViewById(R.id.info_grid);
        ArrayList<InfoModel> sessionArrayList = new ArrayList<InfoModel>();
        sessionArrayList.add(new InfoModel("blaah", "Title"));

        mButtonMaps = (Button) findViewById(R.id.info_button_maps);
        mButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Opna MAPS VIEW
                System.out.println("MAPS TAKKI");
                Intent intent = new Intent(sessionInfoActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}