package main.service.dto;

public class DTOComment {

    private int id;
    private long timestamp;
    private String text;
    private DTOUser user;

    public DTOComment(int id, long timestamp, String text, DTOUser user) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DTOUser getUser() {
        return user;
    }

    public void setUser(DTOUser user) {
        this.user = user;
    }
}
