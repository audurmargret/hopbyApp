package is.hi.hbv601g.hopby.activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.networking.NetworkController;
import is.hi.hbv601g.hopby.services.UserService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {
    private Button mButtonSignup;
    private Button mButtonBack;


    private TextInputEditText mName;
    private TextInputEditText mUserName;
    private TextInputEditText mPassword;

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
                mName = findViewById(R.id.input_name);
                mUserName = findViewById(R.id.input_user);
                mPassword = findViewById(R.id.input_passw);

                boolean exist = mUserService.userExist(String.valueOf(mUserName.getEditableText()));

                boolean isOk = true;
                if (exist) {
                    mUserName.setError("Username is already in use");
                    isOk = false;
                }
                if (mUserName.length() == 0) {
                    mUserName.setError("Required field");
                    isOk = false;
                }
                if (mName.length() == 0) {
                    mName.setError("Required field");
                    isOk = false;
                }
                if (mPassword.length() == 0){
                    mPassword.setError("Required field");
                    isOk = false;
                }

                if(isOk){
                    mUserService.signup(String.valueOf(mName.getEditableText()), String.valueOf(mUserName.getEditableText()), String.valueOf(mPassword.getEditableText()));
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