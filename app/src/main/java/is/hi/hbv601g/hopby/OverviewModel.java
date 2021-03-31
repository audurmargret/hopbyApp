package is.hi.hbv601g.hopby;

public class OverviewModel {
    private String title;
    private String location;
    private String time;
    private String date;
    private String slots;
    private String availableSlots;

    public OverviewModel(String title, String location, String time, String date, String slots, String availableSlots) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.date = date;
        this.slots = slots;
        this.availableSlots = availableSlots;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }
}
