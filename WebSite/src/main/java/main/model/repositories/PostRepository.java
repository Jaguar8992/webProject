package main.model.repositories;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface PostRepository  extends JpaRepository <Post, Integer> {

    @Query("FROM Post WHERE moderation_status='ACCEPTED' and is_active=1 ORDER BY time")
    List <Post> getPostsAllTime();

    // --- Count ---
    @Query("SELECT count(*) FROM Post WHERE moderation_status=\'ACCEPTED\' and is_active=1 and time < CURRENT_TIMESTAMP()")
    long count();

    @Query("SELECT count(*) FROM Post WHERE moderation_status=\'ACCEPTED\' and is_active=1 " +
            "and time < CURRENT_TIMESTAMP() and title LIKE CONCAT(:query,'%')")
    long countByQuery(@Param("query") String query);

    @Query("SELECT count(*) FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.name =:tag")
    int countByTagName (@Param("tag") String tag);

    @Query("SELECT count(*) FROM Post WHERE DATE(time) = :date and moderationStatus=\'ACCEPTED\' and isActive=1 ")
    int countByDate(@Param("date") Date date);

    @Query("SELECT count(*) FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.id =:tag")
    int countByTagId(@Param("tag") Integer tag);

    // --- Get List ---
    @Query ("FROM Post where moderationStatus=\'ACCEPTED' and isActive=1 " +
            "and time < CURRENT_TIMESTAMP() and title LIKE CONCAT(:query,'%')" +
            " ORDER BY time DESC")
    List<Post> getByQuery (@Param("query") String query, Pageable pageable);

    @Query ("FROM Post where moderationStatus=\'ACCEPTED' and isActive=1 " +
            "and time < CURRENT_TIMESTAMP() ORDER BY time DESC")
    List<Post> getRecent (Pageable pageable);

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

    @Query("FROM Post WHERE YEAR(time)= :year and moderationStatus=\'ACCEPTED\' and isActive=1 ORDER BY time")
    List <Post> getByYear (@Param("year") Integer year);

    @Query ("FROM Post where moderationStatus=\'ACCEPTED' and isActive=1 " +
            "and time < CURRENT_TIMESTAMP() and DATE(time) = :date")
    List<Post> getPostsByDate (@Param("date") Date date, Pageable pageable);

    @Query ("FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.name =:tag")
    List<Post> getPostsByTag (@Param("tag") String tag, Pageable pageable);
}

