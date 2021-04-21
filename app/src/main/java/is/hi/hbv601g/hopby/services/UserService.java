package is.hi.hbv601g.hopby.services;

import android.util.Log;

import java.util.List;

import is.hi.hbv601g.hopby.activities.SessionOverviewActivity;
import is.hi.hbv601g.hopby.entities.User;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;

public class UserService {

    private List<User> mUserBank;
    NetworkController mNetworkController;


    public UserService(NetworkController networkController) {
        mNetworkController = networkController;
    }

    public void setUserBank() {
        mNetworkController.getUsers(new NetworkCallback<List<User>>() {
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
    public String getNameForUser(String username){
        if(mUserBank== null ) {
            return username;
        }
        else {
            for (int i = 0; i < mUserBank.size(); i++) {
                if (mUserBank.get(i).getUserName().equals(username)) {
                    return mUserBank.get(i).getName();
                }
            }
        }
        return null;
    }

    public void signup(String name, String username, String password) {
        User newUser = new User(name, username, password);
        mNetworkController.addUser(newUser, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                mUserBank.add(result);
                Log.d("UserService", "User added to bank " + result.getUserName());
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("UserService", "Failed to add user " + errorString);
            }
        });
    }


    public boolean userExist(String username) {
        int len = mUserBank.size();
        for(int i=0; i<len; i++) {
            if(mUserBank.get(i).getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User login(String username, String password){
        int len = mUserBank.size();
        for(int i=0; i<len; i++) {
            if(mUserBank.get(i).getUserName().equals(username)) {
                if(mUserBank.get(i).getPassword().equals(password)) {
                    return mUserBank.get(i);
                }
            }
        }
        return null;
    }
}
