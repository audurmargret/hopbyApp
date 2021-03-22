package is.hi.hbv601g.hopby;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Session {

    private long id;
    private String title;
    private String location;
    private LocalDate date;
    private LocalTime time;
    private List<User> users;
    private int slotsAvailable;
    private int slots;
    private int hobbyId;
    private String description;

    public Session() {
    }

    public Session(String title, String location, LocalDate date, LocalTime time, int slots, int hobbyId, String description) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.slots = slots;
        this.slotsAvailable = slots;
        this.hobbyId = hobbyId;
        this.description = description;

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(User user) {
        this.users.add(user);
        System.out.println("Bæta við user í session lista");
        System.out.println("listinn: " + users);
        slotsAvailable--;
    }

    public void removeUser(User user) {
        users.remove(user);
        System.out.println("Taka " + user.getUserName() + " úr lista");
        System.out.println("listinn: " + users);
        System.out.println("user: " + user);
        slotsAvailable++;
    }

    public int getSlotsAvailable() {
        return slotsAvailable;
    }

    public void setSlotsAvailable(int slotsAvailable) {
        this.slotsAvailable = slotsAvailable;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(int hobbyId) {
        this.hobbyId = hobbyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
