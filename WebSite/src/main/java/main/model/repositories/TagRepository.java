package main.model.repositories;

import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.IThrottledTemplateProcessor;

public interface TagRepository extends CrudRepository <Tag, Integer> {

    @Query(value = "SELECT * FROM tags JOIN tag2post ON tag2post.tag_id = tags.id JOIN posts ON posts.id = tag2post.post_id" +
            " WHERE name LIKE CONCAT(:query,'%') GROUP BY tags.id ORDER BY count(tag2post.id) DESC", nativeQuery = true)
    public Iterable <Tag> getByQuery (@Param("query") String query);

}
