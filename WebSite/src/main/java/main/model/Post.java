package main.model;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "posts")

public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private int isActive;

    @Column(name = "moderation_status", nullable = false)
    private String moderationStatus = "NEW";

    @Column(name = "moderation_id", nullable = true)
    private Integer moderationId;

    @ManyToOne (cascade = CascadeType.ALL)
    private User user;

    @Column (nullable = false, columnDefinition = "timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time = new Date();

    @Column (nullable = false)
    private String title;

    @Column (nullable = false)
    private String text;

    @Column (name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany (cascade = CascadeType.ALL)
    private List <PostComment> postComments;

    @OneToMany (cascade = CascadeType.ALL)
    private List <PostVote> postVotes;

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable (name = "tags2post",
            joinColumns = {@JoinColumn (name = "post_id")},
            inverseJoinColumns = {@JoinColumn (name = "tag_id")})
    private List <Tag> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModerationId() {
        return moderationId;
    }

    public void setModerationId(int moderationId) {
        this.moderationId = moderationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(List<PostVote> postVotes) {
        this.postVotes = postVotes;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
