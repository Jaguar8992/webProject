package main.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")

public class PostVote {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "user_id", nullable = false)
    private int userId;

    @ManyToOne (cascade = CascadeType.ALL)
    private int postId;

    @Column (nullable = false)
    @Type(type = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(nullable = false)
    private int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
