package main.api.response;

public class CommentResponse {

    private int id;

    public CommentResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
