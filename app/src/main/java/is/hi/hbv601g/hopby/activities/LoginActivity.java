package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.entities.User;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.UserService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private Button mButtonLogin;
    private Button mButtonSignup;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        NetworkController networkController = NetworkController.getInstance(this);
        mUserService = new UserService(networkController);

        mUserService.setUserBank();

        mButtonLogin = (Button) findViewById(R.id.button_confirm);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername = findViewById(R.id.input_user);
                mPassword = findViewById(R.id.input_passw);

                Log.d("LoginActivity", "Reyna að finna út ur null reference " + mUserService);
                Log.d("LoginActivity", "Reyna að finna út ur null reference " + mUsername);
                Log.d("LoginActivity", "Reyna að finna út ur null reference " + mPassword);

                User user = mUserService.login(String.valueOf(mUsername.getEditableText()), String.valueOf(mPassword.getEditableText()));

                if (user != null) {
                    SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("loggedInUser", user.getUserName() );
                    editor.putString("loggedInName", user.getName());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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