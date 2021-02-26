package main.model.repositories;

import main.model.Post;
import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("FROM Tag tag JOIN TagToPost tp ON tp.tagId = tag.id JOIN Post post ON post.id = tp.postId" +
            " WHERE tag.name LIKE CONCAT(:query,'%') GROUP BY tag.id ORDER BY count(post.id) DESC")
    List<Tag> getByQuery (@Param("query") String query);

    @Query("SELECT tag.name FROM Tag tag LEFT JOIN TagToPost tp ON tp.tagId = tag.id " +
            "LEFT JOIN Post post ON post.id = tp.postId " +
            "WHERE post = :post")
    List <String> getNameByPost (@Param("post") Post post);

}
