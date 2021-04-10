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
import is.hi.hbv601g.hopby.activities.SessionOverviewActivity;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.networking.NetworkCallback;
import is.hi.hbv601g.hopby.networking.NetworkController;

public class SessionService {

    private Button mButtonInfo;
    private List<Session> mSessionBank;

    private NetworkController mNetworkController;
    private SessionOverviewActivity mSessionOverviewActivity;

    public SessionService(NetworkController networkController, SessionOverviewActivity sessionOverviewActivity) {
        mNetworkController = networkController;
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


}



