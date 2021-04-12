package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.User;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.UserService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mTextInputEditTextUsername;
    private TextInputEditText mTextInputEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonSignup;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkController networkController = NetworkController.getInstance(this);
        mUserService = new UserService(networkController);


        mButtonLogin = (Button) findViewById(R.id.button_confirm);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextInputEditTextUsername = findViewById(R.id.input_user);
                mTextInputEditTextPassword = findViewById(R.id.input_passw);

                Log.d("LoginActivity", String.valueOf(mTextInputEditTextUsername.getEditableText()) + " " + String.valueOf(mTextInputEditTextPassword.getEditableText()));
                User user = mUserService.login(String.valueOf(mTextInputEditTextUsername.getEditableText()), String.valueOf(mTextInputEditTextPassword.getEditableText()));

                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, SessionOverviewActivity.class);
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Username and password don't match";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });

        mButtonSignup = (Button) findViewById(R.id.button_signup);
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: opna signup
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}