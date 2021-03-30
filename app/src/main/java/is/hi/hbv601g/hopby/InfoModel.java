package is.hi.hbv601g.hopby;

public class InfoModel {
    private String type;
    private String string;
    public InfoModel(String string, String type) {
        this.string = string;
        this.type = type;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
