package is.hi.hbv601g.hopby.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.services.FilterService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.chip.ChipGroup;

public class FilterActivity extends AppCompatActivity {

    private Button mButtonSubmit;
    private Button mButtonBack;
    private ChipGroup mChipGroupHobby;
    private ChipGroup mChipGroupDay;
    private ChipGroup mChipGroupTime;
    private String mLoggedInUser;

    private FilterService mFilterService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Connect to service
        mFilterService = new FilterService();

        // Get name of logged in user
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        mLoggedInUser = preferences.getString("loggedInName", "");

        // Submit filter button
        mButtonSubmit = (Button) findViewById(R.id.filter_button);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                mChipGroupHobby = (ChipGroup) findViewById(R.id.chip_group_hobby);
                mChipGroupDay = (ChipGroup) findViewById(R.id.chip_group_days);
                mChipGroupTime = (ChipGroup) findViewById(R.id.chip_group_time);

                boolean[] hobbies = mFilterService.getBoolean(mChipGroupHobby);
                boolean[] time = mFilterService.getBoolean(mChipGroupTime);
                boolean[] day = mFilterService.getBoolean(mChipGroupDay);

                Intent intent = new Intent(FilterActivity.this, SessionOverviewActivity.class);
                intent.putExtra("filter", true);
                intent.putExtra("hobbies", hobbies);
                intent.putExtra("times", time);
                intent.putExtra("days", day);

                startActivity(intent);
            }
        });

        // Back button
        mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}