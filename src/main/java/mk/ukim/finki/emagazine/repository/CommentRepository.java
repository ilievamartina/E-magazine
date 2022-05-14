package mk.ukim.finki.emagazine.repository;

import mk.ukim.finki.emagazine.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
