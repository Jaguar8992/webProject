package main.model.repositories;

import main.model.Post;
import main.model.User;
import org.springframework.data.domain.Page;
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
    @Query("SELECT count(*) FROM Post WHERE moderation_status='ACCEPTED' and is_active=1 and time < CURRENT_TIMESTAMP()")
    long count(@Param("date") Date date);

    @Query("SELECT count(*) FROM Post WHERE DATE(time) = :date and moderationStatus='ACCEPTED' and isActive=1 ")
    int countByDate(@Param("date") Date date);

    @Query("SELECT count(*) FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.id =:tag")
    int countByTagId(@Param("tag") Integer tag);

    // --- Get List ---
    @Query ("FROM Post post where moderationStatus='ACCEPTED' and isActive=1 " +
            "and time < :date and title LIKE CONCAT(:query,'%')" +
            " ORDER BY time DESC")
    Page<Post> getByQuery (@Param("query") String query, Pageable pageable, @Param("date") Date date);

    @Query ("FROM Post post WHERE moderationStatus='ACCEPTED' and isActive=1 " +
            "and time < :date ORDER BY time DESC")
    Page<Post> getRecent (Pageable pageable, @Param("date") Date date);

    @Query("SELECT post FROM Post post LEFT JOIN PostComment pc ON pc.post = post.id" +
            " WHERE post.moderationStatus='ACCEPTED' and post.isActive=1 " +
            "and post.time < :date GROUP BY post.id ORDER BY count(pc.id) DESC")
    Page<Post> getPopular(Pageable pageable, @Param("date") Date date);

    @Query("SELECT post FROM Post post LEFT JOIN PostVote pv ON pv.post = post.id " +
            "WHERE post.moderationStatus='ACCEPTED' and post.isActive=1 " +
            "and post.time < :date GROUP BY post.id " +
            "ORDER BY sum(CASE WHEN pv.value=1 THEN 1 ELSE 0 END) DESC")
    Page<Post> getBest (Pageable pageable, @Param("date") Date date);

    @Query("FROM Post post WHERE moderationStatus='ACCEPTED' and isActive=1 " +
            "and time < :date ORDER BY time")
    Page<Post> getEarly (Pageable pageable, @Param("date") Date date);

    @Query("FROM Post post WHERE YEAR(time)= :year and moderationStatus=\'ACCEPTED\' and isActive=1 ORDER BY time")
    List <Post> getByYear (@Param("year") Integer year);

    @Query ("FROM Post post where moderationStatus='ACCEPTED' and isActive=1 " +
            "and time < :currentDate and DATE(time) = :date")
    Page<Post> getPostsByDate (@Param("date") Date date, Pageable pageable, @Param("currentDate") Date currentDate);

    @Query ("SELECT post FROM Post post JOIN TagToPost tp ON tp.postId = post.id" +
            " JOIN Tag tag ON tag.id = tp.tagId WHERE tag.name =:tag GROUP BY post.id")
    Page<Post> getPostsByTag (@Param("tag") String tag, Pageable pageable);

    // GetMy list

    @Query("FROM Post post WHERE user =:user AND isActive = 0")
    Page<Post> findInactive (@Param("user") User user, Pageable pageable);

    @Query("FROM Post post WHERE user =:user AND isActive = 1 AND moderationStatus = 'NEW'")
    Page<Post> findPending (@Param("user") User user, Pageable pageable);

    @Query("FROM Post post WHERE user =:user AND isActive = 1 AND moderationStatus = 'DECLINED'")
    Page<Post> findDeclined (@Param("user") User user, Pageable pageable);

    @Query("FROM Post post WHERE user =:user AND isActive = 1 AND moderationStatus = 'ACCEPTED'")
    Page<Post> findPublished (@Param("user") User user, Pageable pageable);

    // Get count for moderation

    @Query("SELECT count(*) FROM Post WHERE isActive = 1 AND moderationStatus = 'NEW'")
    int countNewPostsForModeration();

    // Get list for moderation

    @Query("FROM Post post WHERE isActive = 1 AND moderationStatus = 'NEW'")
    Page<Post> findNewPostsForModeration(Pageable pageable);

    @Query("FROM Post post WHERE moderationId = :id AND isActive = 1 AND moderationStatus = 'DECLINED'")
    Page<Post> findDeclinedPostsForModeration(@Param("id") Integer id, Pageable pageable);

    @Query("FROM Post post WHERE moderationId = :id AND isActive = 1 AND moderationStatus = 'ACCEPTED'")
    Page<Post> findAcceptedPostsForModeration(@Param("id") Integer id, Pageable pageable);

}

