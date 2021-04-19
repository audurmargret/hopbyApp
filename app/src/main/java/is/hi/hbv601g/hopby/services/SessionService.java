package is.hi.hbv601g.hopby.services;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.hopby.activities.MySessionsActivity;
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
    private MySessionsActivity mMySessionsActivity;
    private static Session mSessionForMaps;

    private int mHobbyCount;
    private int mTimeCount;
    private int mDayCount;

    private long sessionId;

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

    public void getMySession(MySessionsActivity mySessionActivity, String username) {
        mMySessionsActivity = mySessionActivity;
        mNetworkController.getMySessions(new NetworkCallback<List<Session>>() {
            @Override
            public void onSuccess(List<Session> result) {
                mSessionBank = result;
                mMySessionsActivity.updateSessions(mSessionBank);
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to get sessions " + errorString);
            }
        }, username);
    }

    public void getSession(SessionInfoActivity sessionInfoActivity, long id) {
        mSessionInfoActivity = sessionInfoActivity;
        mNetworkController.getSession(new NetworkCallback<Session>() {
            @Override
            public void onSuccess(Session result) {
                mSessionInfoActivity.updateSession(result);
                mSessionForMaps = result;
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to get sessions " + errorString);
            }
        }, id);
    }

    public void joinSession(long id, String username, String path) {
        mNetworkController.joinSession(new NetworkCallback<Session>() {
            @Override
            public void onSuccess(Session result) {
                Log.d("SessionService", "joinSession success");

            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "joinSession failure" + errorString + " ID: " + id + " USERNAME: " + username + " PATH: " + path);
            }
        }, path, id, username);
    }

    public void deleteSession(long id) {
        mNetworkController.deleteSession(new NetworkCallback<String>(){
            @Override
            public void onSuccess(String response) {
                Log.d("SessionService", "deleteSession success");
            }
            @Override
            public void onFailure(String errorString)  {
                Log.d("SessionService", "deleteSession failure");
            }
        }, id);
    }

    // TODO avoid static methods
    public static Session getSessionForMaps(){
        return mSessionForMaps;
    }


    public long addSession(TextInputEditText title, CalendarView date, String time, TextInputEditText slots, String hobbyId, TextInputEditText description, TextInputEditText location, String host) {
        Session newSession = format( title, date, time, slots, hobbyId, description, location, host);
        mNetworkController.addSession(newSession, host, new NetworkCallback<Session>() {
            @Override
             public void onSuccess(Session result) {
                //mSessionBank.add(result);
                Log.d("SessionService", "Session added to bank " + result.getId());
                sessionId = result.getId();
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("SessionService", "Failed to add session " + errorString);
            }
        });

        return sessionId;
    }


    public Session format(TextInputEditText title, CalendarView date, String time, TextInputEditText slots, String hobby, TextInputEditText description, TextInputEditText location, String host) {
        String titleString = title.getText().toString();
        String descriptionString = description.getText().toString();
        String locationString = location.getText().toString();
        int slotsInt = Integer.parseInt(slots.getText().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date.getDate());

        int hobbyInt;
        if(hobby.equals("Football")) hobbyInt = 1;
        else if(hobby.equals("Basketball")) hobbyInt = 2;
        else hobbyInt = 3;


        Session session = new Session(0, titleString, locationString, dateString, time, slotsInt, hobbyInt, descriptionString, host);
        return session;
    }

    public List<String> getHobbies() {
        List<String> hobbies = new ArrayList<String>();
        hobbies.add("Football");
        hobbies.add("Basketball");
        hobbies.add("Hike");

        return hobbies;
    }

    public String filterText(boolean[] h, boolean[] t, boolean[] d) {
        if(mHobbyCount == 0 && mTimeCount == 0 && mDayCount == 0) {
            return "No filters applied";
        }

        String hobby = "";
        String day = "";
        String time = "";
        String returnString = "";

        if(mHobbyCount>0){
            if(mHobbyCount == 1){
                hobby = "Hobby: ";
            } else {
                hobby = "Hobbies: ";
            }
            for (int i = 0; i<h.length; i++) {
                if (h[i]){
                    hobby = hobby.concat(String.valueOf(i));
                }
            }
            hobby = hobby.replace("0", "Football ");
            hobby = hobby.replace("1", " Basketball ");
            hobby = hobby.replace("2", " Hike");
            hobby = hobby.replace("  ", " - ");
            hobby = hobby.concat("\n");
        }

        if(mTimeCount>0){
            time = "Time: ";
            for (int i = 0; i<t.length; i++) {
                if (t[i]){
                    time = time.concat(String.valueOf(i));
                }
            }
            time = time.replace("0", "Morning ");
            time = time.replace("1", " Afternoon ");
            time = time.replace("2", " Evening");
            time = time.replace("  ", " - ");
            time = time.concat("\n");

        }

        if(mDayCount>0){
            if(mDayCount == 1){
                day = "Day: ";
            } else {
                day = "Days: ";
            }
            for (int i = 0; i<d.length; i++) {
                if (d[i]){
                    day = day.concat(String.valueOf(i));
                }
            }
            day = day.replace("0", "Mon ");
            day = day.replace("1", " Tue ");
            day = day.replace("2", " Wed ");
            day = day.replace("3", " Thu ");
            day = day.replace("4", " Fri ");
            day = day.replace("5", " Sat ");
            day = day.replace("6", " Sun");
            day = day.replace("  ", " - ");
            day = day.concat("\n");
        }
        returnString = returnString.concat(hobby).concat(time).concat(day);
        return returnString;
    }


    public ArrayList<Session> filter(List<Session> sessionBank, boolean[] h, boolean[] t, boolean[]d) throws ParseException {
        ArrayList<Session> sessionArrayList = new ArrayList<Session>();
        int bankLength = sessionBank.size();

        mHobbyCount = 0;
        mTimeCount = 0;
        mDayCount = 0;

        for(int i=0; i<h.length; i++) {
            if (h[i]) {
                mHobbyCount++;
            }
        }

        for(int i=0; i<t.length; i++) {
            if (t[i]) {
                mTimeCount++;
            }
        }

        for(int i=0; i<d.length; i++) {
            if (d[i]) {
                mDayCount++;
            }
        }

        int[] day = new int[mDayCount];
        int indexD = 0;
        for(int i = 0; i<d.length; i++) {
            if(d[i]) {
                if(i==6) day[indexD++] = 1;
                else day[indexD++] = i+2;
            }
        }

        int[] time = new int[mTimeCount*2];
        int indexT = 0;
        for (int i=0; i<t.length; i++) {
            if(t[i]) {
                if(i == 0) {
                    time[indexT++] = 0000;
                    time[indexT++] = 1200;
                }
                if(i == 1) {
                    time[indexT++] = 1200;
                    time[indexT++] = 1800;
                }
                if(i == 2) {
                    time[indexT++] = 1800;
                    time[indexT++] = 2400;
                }
            }
        }

        int[] hobby = new int[mHobbyCount];
        int indexH = 0;
        for(int i = 0; i<h.length; i++) {
            if(h[i]) {
                hobby[indexH++] = i+1;
            }
        }
        boolean checkTime = true;
        if (mTimeCount == 0 || mTimeCount == t.length) {
            checkTime = false;
        }

        boolean checkDay = true;
        if(mDayCount == 0 || mDayCount == d.length) {
            checkDay = false;
        }

        boolean checkHobby = true;
        if (mHobbyCount == 0 || mHobbyCount == h.length) {
            checkHobby = false;
        }

        System.out.println("CHECK TIME " + checkTime + " Count: " + mTimeCount);
        System.out.println("CHECK DAY " + checkDay + " Count: " + mDayCount);
        System.out.println("CHECK HOBBY " + checkHobby + " Count: " + mHobbyCount);
        boolean fitsDay = false;
        boolean fitsTime = false;
        boolean fitsHobby = false;

        for(int i = 0; i<bankLength; i++) {
            if(checkDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(sessionBank.get(i).getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                for(int value : day) {
                    if(dayOfWeek == value) {
                        fitsDay = true;
                        break;
                    }
                }
            } else {
                fitsDay = true;
            }

            if(checkTime) {
                for (int iTime = 0; iTime < time.length; iTime += 2) {
                    SimpleDateFormat formatTime = new SimpleDateFormat("HHmm", Locale.getDefault());
                    int sessionTime = Integer.parseInt(sessionBank.get(i).getTime().replace(":", "").substring(0, 4));
                    if (sessionTime >= time[iTime] && sessionTime < time[iTime + 1]) {
                        fitsTime = true;
                        break;
                    }
                }
            } else {
                fitsTime = true;
            }

            if(checkHobby) {
                for (int index = 0; index < hobby.length; index++) {

                    if (sessionBank.get(i).getHobbyId() == hobby[index]) {
                        fitsHobby = true;
                        break;
                    }
                }
            } else {
                fitsHobby = true;
            }

            if(fitsDay && fitsTime && fitsHobby) {
                System.out.println("YAY PASSAR");
                sessionArrayList.add(sessionBank.get(i));
            }
            fitsDay = false;
            fitsTime = false;
            fitsHobby = false;
        }


        return sessionArrayList;
    }

    public String getUserList(Session session) {
        List<User> userlist = session.getUsers();
        String userListString = "";
        if(userlist != null) {
            int length = userlist.size();
            for(int i = 0; i<length; i++) {
                userListString = userListString.concat(userlist.get(i).getName()).concat("\n");
            }
        }
        return userListString;
    }

    public boolean isUserInSession(Session session, String username) {
        List<User> userList = session.getUsers();
        for(int i=0; i<userList.size(); i++) {
            if(userList.get(i).getUserName().equals(username))
                return true;
        }
        return false;
    }

    public boolean isUserHost(Session session, String username) {
        List<User> userList = session.getUsers();
        if(userList.get(0).getUserName().equals(username)) return true;
        return false;
    }
}



