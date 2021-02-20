package main.model.repositories;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends CrudRepository <PostVote, Integer> {

    @Query(value = "SELECT count(*) FROM post_votes WHERE post_id =:post and value = :value", nativeQuery = true)
    public int getCountForPost (@Param("post") Integer post, @Param("value") Integer value);
}
