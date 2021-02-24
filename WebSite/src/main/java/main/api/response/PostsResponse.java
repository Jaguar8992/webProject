package main.api.response;

import main.service.dto.DTOPost;

import java.util.List;

public class PostsResponse {

    private long count;
    private List<DTOPost> posts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<DTOPost> getPosts() {
        return posts;
    }

    public void setPosts(List<DTOPost> posts) {
        this.posts = posts;
    }
}
