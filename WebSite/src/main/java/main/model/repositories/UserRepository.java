package main.model.repositories;

import main.model.Post;
import main.model.PostComment;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("FROM User user LEFT JOIN Post post ON post.user = user.id WHERE post = :post")
    User getByPost (@Param("post") Post post);

    @Query("FROM User user LEFT JOIN PostComment pc ON pc.user = user.id WHERE pc = :postComment")
    User getByComment (@Param("postComment") PostComment postComment);

    User findByEmail(String email);

    User findByName(String name);

}
