package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mTextInputEditTextUsername;
    private TextInputEditText mTextInputEditTextPassword;
    private Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mButtonLogin = (Button) findViewById(R.id.button_confirm);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: login
                mTextInputEditTextUsername = findViewById(R.id.input_user);
                mTextInputEditTextPassword = findViewById(R.id.input_passw);

                Log.d("LoginActivity", String.valueOf(mTextInputEditTextUsername.getEditableText()) + " " + String.valueOf(mTextInputEditTextPassword.getEditableText()));


                //Intent intent = new Intent(LoginActivity.this, SessionOverviewActivity.class);
                //startActivity(intent);

            }
        });
    }
}