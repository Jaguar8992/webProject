package main.api.response;

import main.service.ServiceData.PostBody;

import java.util.List;

public class PostsResponse {

    private long count;
    private List<PostBody> posts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostBody> getPosts() {
        return posts;
    }

    public void setPosts(List<PostBody> posts) {
        this.posts = posts;
    }
}
