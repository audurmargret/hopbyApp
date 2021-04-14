package is.hi.hbv601g.hopby.services;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                Log.d("SessionService", "First session in bank " + mSessionBank.get(0).getTitle() + ", slots: " + mSessionBank.get(0).getSlots());
                mSessionOverviewActivity.updateSessions(mSessionBank);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to get sessions " + errorString);
            }
        });
    }

    public void getSession(SessionInfoActivity sessionInfoActivity, long id) {
        mSessionInfoActivity = sessionInfoActivity;
        mNetworkController.getSession(new NetworkCallback<Session>() {
            @Override
            public void onSuccess(Session result) {
                mSessionInfoActivity.updateSession(result);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to get sessions " + errorString);
            }
        }, id);

    }

    public void addSession(TextInputEditText title, CalendarView date, TextInputEditText time, TextInputEditText slots, String hobbyId, TextInputEditText description, TextInputEditText location) {
        Session newSession = format( title, date, time, slots, hobbyId, description, location);
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

    public Session format(TextInputEditText title, CalendarView date, TextInputEditText time, TextInputEditText slots, String hobby, TextInputEditText description, TextInputEditText location) {
        String titleString = title.getText().toString();
        String descriptionString = description.getText().toString();
        String locationString = location.getText().toString();
        String timeString = time.getText().toString();
        int slotsInt = Integer.parseInt(slots.getText().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date.getDate());

        int hobbyInt;
        if(hobby.equals("Hike")) hobbyInt = 3;
        else if(hobby.equals("Basketball")) hobbyInt = 2;
        else hobbyInt = 1;

        Session session = new Session(0, titleString, locationString, dateString, timeString, slotsInt, hobbyInt, descriptionString);
        Log.d("SessionService", "FORMAT");
        return session;
    }
    public List<String> getHobbies() {
        List<String> hobbies = new ArrayList<String>();
        hobbies.add("Football");
        hobbies.add("Basketball");
        hobbies.add("Hike");

        return hobbies;
    }


}



