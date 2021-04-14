package is.hi.hbv601g.hopby.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.RequiresApi;
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

    public void getAllSession(SessionOverviewActivity sessionOverviewActivity, boolean filter) {
        mSessionOverviewActivity = sessionOverviewActivity;
        mNetworkController.getSessions(new NetworkCallback<List<Session>>() {
            @Override
            public void onSuccess(List<Session> result) {
                mSessionBank = result;
                Log.d("SessionService", "First session in bank " + mSessionBank.get(0).getTitle() + ", slots: " + mSessionBank.get(0).getSlots());
                mSessionOverviewActivity.updateSessions(mSessionBank, filter);
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


    public ArrayList<Session> filter(List<Session> sessionBank, boolean[] h, boolean[] t, boolean[]d) throws ParseException {
        ArrayList<Session> sessionArrayList = new ArrayList<Session>();
        int bankLength = sessionBank.size();

        int hobbyCount = 0;
        int timeCount = 0;
        int dayCount = 0;

        for(int i=0; i<h.length; i++) {
            if (h[i]) {
                hobbyCount++;
            }
        }

        for(int i=0; i<t.length; i++) {
            if (t[i]) {
                timeCount++;
            }
        }

        for(int i=0; i<d.length; i++) {
            if (d[i]) {
                dayCount++;
            }
        }
        if((hobbyCount == 0 || hobbyCount == h.length) && (timeCount == 0 || timeCount == t.length) && (dayCount == 0 || dayCount == d.length)) {
            // Enginn filter hér, er hægt að skila einhverju?
            return null;
        }

        int[] day = new int[dayCount];
        int indexD = 0;
        for(int i = 0; i<d.length; i++) {
            if(d[i]) {
                if(i==6) day[indexD++] = 1;
                else day[indexD++] = i+2;
            }
        }




        for(int i = 0; i<bankLength; i++) {
            // tékka hvort dags passar:
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(sessionBank.get(i).getDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            System.out.println(date.toString().substring(0,3));

            for(int ii = 0; ii<day.length; ii++) {
                if(dayOfWeek == day[ii]) {
                    sessionArrayList.add(sessionBank.get(i));
                }
            }

        }
        return sessionArrayList;
    }

}



