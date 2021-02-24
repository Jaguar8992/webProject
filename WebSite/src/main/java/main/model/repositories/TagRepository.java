package main.model.repositories;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.IThrottledTemplateProcessor;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("FROM Tag tag JOIN TagToPost tp ON tp.tagId = tag.id JOIN Post post ON post.id = tp.postId" +
            " WHERE name LIKE CONCAT(:query,'%') GROUP BY tag.id ORDER BY count(post.id) DESC")
    List<Tag> getByQuery (@Param("query") String query);

}
