package main.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "post_comments")

public class PostComment {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    private Integer parentId;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column (nullable = false, columnDefinition = "timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column (nullable = false)
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
