package is.hi.hbv601g.hopby.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.services.FilterService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Button mButtonSubmit;
    private ChipGroup mChipGroupHobby;
    private ChipGroup mChipGroupDay;
    private ChipGroup mChipGroupTime;

    private FilterService mFilterService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mFilterService = new FilterService();

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
    }
}