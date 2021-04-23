package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;

import android.os.Bundle;
import android.widget.TextView;

public class SpinnerHobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_hobby);

        // Set the drop down list for hobbies in Create Sessions
        TextView textView = (TextView) findViewById(R.id.spinner_hobby_item);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.get("data").toString();
        textView.setText(data);

    }
}