package main.service.dto;

public class DTOComment {

    private int id;
    private long timestamp;
    private String text;
    private DTOCommentUser user;

    public DTOComment(int id, long timestamp, String text, DTOCommentUser user) {
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

    public DTOCommentUser getUser() {
        return user;
    }

    public void setUser(DTOCommentUser user) {
        this.user = user;
    }
}
