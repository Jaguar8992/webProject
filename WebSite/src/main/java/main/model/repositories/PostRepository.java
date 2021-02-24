package main.model.repositories;

import main.model.Post;
import org.hibernate.mapping.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository  extends JpaRepository <Post, Integer> {

    @Query("SELECT count(*) FROM Post WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()")
    long count();

    @Query ("FROM Post where moderationStatus=\'ACCEPTED' and isActive=1 " +
            "and time < CURRENT_TIMESTAMP() and title LIKE CONCAT(:query,'%')" +
            " ORDER BY time DESC")
    List<Post> getRecent (@Param("query") String query, Pageable pageable);

    @Query("FROM Post post LEFT JOIN PostComment pc ON pc.post = post.id" +
            " WHERE post.moderationStatus=\'ACCEPTED\' and post.isActive=1 " +
            "and post.time < CURRENT_TIMESTAMP() GROUP BY post.id ORDER BY count(pc.id) DESC")
    List<Post> getPopular(Pageable pageable);

    @Query("FROM Post post LEFT JOIN PostVote pv ON pv.post = post.id " +
            "WHERE post.moderationStatus=\'ACCEPTED\' and pv.value = 1 and post.isActive=1 " +
            "and post.time < CURRENT_TIMESTAMP() GROUP BY post.id ORDER BY count(pv.id) DESC")
    List<Post> getBest (Pageable pageable);

    @Query("FROM Post WHERE moderationStatus=\'ACCEPTED\' and isActive=1 " +
            "and time < CURRENT_TIMESTAMP() ORDER BY time")
    List<Post> getEarly (Pageable pageable);

    @Query("SELECT count(*) FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.id =:tag")
    int getCountByTag (@Param("tag") Integer tag);
}

