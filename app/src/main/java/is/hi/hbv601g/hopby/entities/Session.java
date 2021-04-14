package is.hi.hbv601g.hopby.entities;


import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Session {

    @SerializedName("id")
    private long mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("date")
    private String mDate;
    @SerializedName("time")
    private String mTime;
    @SerializedName("users")
    private List<User> mUsers;
    @SerializedName("slotsAvailable")
    private int mSlotsAvailable;
    @SerializedName("slots")
    private int mSlots;
    @SerializedName("hobbyId")
    private int mHobbyId;
    @SerializedName("description")
    private String mDescription;

    public Session(long id, String title, String location, String date, String time, int slots, int hobbyId, String description) {
        mId = id;
        mTitle = title;
        mLocation = location;
        mDate = date;
        mTime = time;
        mSlots = slots;
        mSlotsAvailable = slots;
        mHobbyId = hobbyId;
        mDescription = description;

    }

    public long getId() {
        return mId;
    }

    public void setId(int mId) {
        mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        mTitle = mTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        mLocation = mLocation;
    }

    public String getDate() {
        //String year = mDate.substring(0,4);
        //String month = mDate.substring(5,7);
        //String day = mDate.substring(8,10);
        //String date = day.concat("-").concat(month).concat("-").concat(year);
        return mDate;
    }

    public void setDate(String mDate) {
        mDate = mDate;
    }

    public String getTime() {
        //return mTime.substring(0,5);
        return mTime;
    }

    public void setTime(String mTime) {
        mTime = mTime;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(User user) {
        mUsers.add(user);
        System.out.println("Bæta við user í session lista");
        System.out.println("listinn: " + mUsers);
        mSlotsAvailable--;
    }

    public void removeUser(User user) {
        mUsers.remove(user);
        System.out.println("Taka " + user.getUserName() + " úr lista");
        System.out.println("listinn: " + mUsers);
        System.out.println("user: " + user);
        mSlotsAvailable++;
    }

    public int getSlotsAvailable() {
        return mSlotsAvailable;
    }

    public void setSlotsAvailable(int mSlotsAvailable) {
        mSlotsAvailable = mSlotsAvailable;
    }

    public int getSlots() {
        return mSlots;
    }

    public void setSlots(int mSlots) {
        mSlots = mSlots;
    }

    public int getHobbyId() {
        return mHobbyId;
    }

    public void setHobbyId(int mHobbyId) {
        mHobbyId = mHobbyId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        mDescription = mDescription;
    }

}
