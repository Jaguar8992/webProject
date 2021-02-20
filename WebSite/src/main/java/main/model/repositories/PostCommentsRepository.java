package main.model.repositories;

import main.model.PostComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentsRepository extends CrudRepository <PostComment, Integer> {

    @Query(value = "SELECT count(*) FROM post_comments WHERE post_id =:post", nativeQuery = true)
    public int getCountForPost (@Param("post") Integer post);
}
