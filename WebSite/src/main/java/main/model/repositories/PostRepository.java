package main.model.repositories;

import main.model.Post;
import org.hibernate.mapping.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface PostRepository  extends CrudRepository <Post, Integer> {

    @Query("SELECT count(*) FROM Post WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()")
    public long count();

    @Query(value = "SELECT * FROM posts WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()" +
            " ORDER BY time DESC LIMIT :offset, :limit", nativeQuery = true)
    public Iterable<Post> getRecent (@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "SELECT * FROM posts JOIN post_comments ON post_comments.post_id = id" +
            "WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()" +
            " ORDER BY count(post_comments.id) DESC LIMIT :offset, :limit", nativeQuery = true)
    public Iterable<Post> getPopular (@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "SELECT * FROM posts JOIN post_votes ON post_votes.post_id = id " +
            "WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()" +
            " ORDER BY count(post_votes.id) DESC LIMIT :offset, :limit", nativeQuery = true)
    public Iterable<Post> getBest (@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "SELECT * FROM posts WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()" +
            " ORDER BY time LIMIT :offset, :limit" , nativeQuery = true)
    public Iterable<Post> getEarly (@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "SELECT count(*) FROM posts JOIN tag2post ON tag2post.post_id = posts.id" +
            " JOIN tags ON tags.id = tag2post.tag_id WHERE tags.id =:tag", nativeQuery = true)
    public int getCountByTag (@Param("tag") Integer tag);
}

