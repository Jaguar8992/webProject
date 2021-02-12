package main.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "tags")
public class Tag {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (nullable = false)
    private String name;

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable (name = "tags2post",
            joinColumns = {@JoinColumn (name = "tag_id")},
            inverseJoinColumns = {@JoinColumn (name = "post_id")})
    private List<Post> post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }
}
