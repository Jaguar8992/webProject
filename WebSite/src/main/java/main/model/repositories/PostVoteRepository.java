package main.model.repositories;

import main.model.Post;
import main.model.PostVote;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query("SELECT count(*) FROM PostVote WHERE post =:post and value = :value")
    int getCountForPost (@Param("post") Post post, @Param("value") Integer value);

    @Query("FROM PostVote WHERE post = :post AND user = :user")
    PostVote findVote (@Param("post") Post post, @Param("user") User user);

}
