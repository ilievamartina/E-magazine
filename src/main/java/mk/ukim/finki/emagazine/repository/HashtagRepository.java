package mk.ukim.finki.emagazine.repository;

import mk.ukim.finki.emagazine.model.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, String> {
}
