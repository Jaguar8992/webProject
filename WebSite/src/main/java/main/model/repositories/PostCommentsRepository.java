package main.model.repositories;

import main.model.Post;
import main.model.PostComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentsRepository extends CrudRepository <PostComment, Integer> {

    @Query("SELECT count(*) FROM PostComment WHERE post =:post")
    int getCountForPost (@Param("post") Post post);

    List<PostComment> findByPost (Post post);
}
