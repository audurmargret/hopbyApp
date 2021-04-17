package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
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

public class SignupActivity extends AppCompatActivity {
    private Button mButtonSignup;
    private Button mButtonBack;


    private TextInputEditText mTextInputEditTextName;
    private TextInputEditText mTextInputEditTextUsername;
    private TextInputEditText mTextInputEditTextPassword;

    private UserService mUserService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        NetworkController networkController = NetworkController.getInstance(this);
        mUserService = new UserService(networkController);

        mButtonSignup = (Button) findViewById(R.id.button_confirm);
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextInputEditTextName = findViewById(R.id.input_name);
                mTextInputEditTextUsername = findViewById(R.id.input_user);
                mTextInputEditTextPassword = findViewById(R.id.input_passw);

                String userName = String.valueOf(mTextInputEditTextUsername.getEditableText());
                Log.d("SignupActivity", userName + " " + String.valueOf(mTextInputEditTextPassword.getEditableText()));
                boolean exist = mUserService.userExist(String.valueOf(mTextInputEditTextUsername.getEditableText()));


                if (exist) {
                    Context context = getApplicationContext();
                    CharSequence text = "Username is already in use";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    mUserService.signup(String.valueOf(mTextInputEditTextName.getEditableText()), String.valueOf(mTextInputEditTextUsername.getEditableText()), String.valueOf(mTextInputEditTextPassword.getEditableText()));
                    Context context = getApplicationContext();
                    CharSequence text = "Success, please login";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    //finish();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}