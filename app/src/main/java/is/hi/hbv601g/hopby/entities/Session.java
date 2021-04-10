package is.hi.hbv601g.hopby.entities;


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
//    @SerializedName("date")
//    private LocalDate mDate;
//    @SerializedName("time")
//    private LocalTime mTime;
    @SerializedName("users")
    private List<User> mUsers;
    @SerializedName("SlotsAvailable")
    private int mSlotsAvailable;
    @SerializedName("Slots")
    private int mSlots;
    @SerializedName("HobbyId")
    private int mHobbyId;
    @SerializedName("Description")
    private String mDescription;

    public Session(String title, String location, LocalDate date, LocalTime time, int slots, int hobbyId, String description) {
        mTitle = title;
        mLocation = location;
//        mDate = date;
//        mTime = time;
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

//    public LocalDate getDate() {
//        return mDate;
//    }
//
//    public void setDate(LocalDate mDate) {
//        mDate = mDate;
//    }
//
//    public LocalTime getTime() {
//        return mTime;
//    }
//
//    public void setTime(LocalTime mTime) {
//        mTime = mTime;
//    }

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
