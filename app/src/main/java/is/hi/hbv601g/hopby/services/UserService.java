package is.hi.hbv601g.hopby.services;

import android.util.Log;

import java.util.List;

import is.hi.hbv601g.hopby.activities.SessionOverviewActivity;
import is.hi.hbv601g.hopby.entities.User;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;

public class UserService {


    private List<User> mUserBank;

    public UserService(NetworkController networkController) {
        networkController.getUsers(new NetworkCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                mUserBank = result;
                Log.d("UserService", "First user in bank " + mUserBank.get(0).getUserName());
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("UserService", "Failed to get users " + errorString);
            }
        });
    }

    public List<User> getUserBank() {
        return mUserBank;
    }

    public boolean userExist( String username, String password) {
        int len = mUserBank.size();
        for(int i=0; i<len; i++) {
            Log.d("UserService", mUserBank.get(i).getUserName() + " " + username);
            if(mUserBank.get(i).getUserName().equals(username)) {
                Log.d("UserService", "notandi er til");
                if(mUserBank.get(i).getPassword().equals(password)) {
                    Log.d("UserService", "Rétt lykilorð");
                    return true;
                }
            }
        }
        return false;
    }
}
