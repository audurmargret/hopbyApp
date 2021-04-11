package is.hi.hbv601g.hopby.services;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.hopby.OverviewAdapter;
import is.hi.hbv601g.hopby.R;
import is.hi.hbv601g.hopby.activities.SessionInfoActivity;
import is.hi.hbv601g.hopby.activities.SessionOverviewActivity;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.entities.User;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;

public class SessionService {

    private Button mButtonInfo;
    private List<Session> mSessionBank;

    private NetworkController mNetworkController;
    private SessionOverviewActivity mSessionOverviewActivity;
    private SessionInfoActivity mSessionInfoActivity;

    public SessionService(NetworkController networkController) {
        mNetworkController = networkController;
    }

    public void getAllSession(SessionOverviewActivity sessionOverviewActivity) {
        mSessionOverviewActivity = sessionOverviewActivity;
        mNetworkController.getSessions(new NetworkCallback<List<Session>>() {
            @Override
            public void onSuccess(List<Session> result) {
                mSessionBank = result;
                Log.d("SessionOverviewActivity", "First session in bank " + mSessionBank.get(0).getTitle());
                mSessionOverviewActivity.updateSessions(mSessionBank);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionOverviewActivity", "Failed to get sessions " + errorString);
            }
        });
    }

    public void getSession(SessionInfoActivity sessionInfoActivity, int id) {
        mSessionInfoActivity = sessionInfoActivity;
        mNetworkController.getSession(new NetworkCallback<Session>() {
            @Override
            public void onSuccess(Session result) {
                Log.d("SessionOverviewActivity", "Result session: " + result.getTitle());
                mSessionInfoActivity.updateSession(result);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionOverviewActivity", "Failed to get session " + errorString);
            }
        });
    }

    public void addSession(String title, String date, String time, int slots, int hobbyId, String description, String location) {
        Session newSession = new Session(title,location,date,time, slots, hobbyId, description);
        Log.d("SessionService", newSession.getTime());
        mNetworkController.addSession(newSession, new NetworkCallback<Session>() {
            @Override
            public void onSuccess(Session result) {
                //mSessionBank.add(result);
                Log.d("SessionService", "Session added to bank " + result.getTitle());
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to add session " + errorString);
            }
        });
    }


}



