package mk.ukim.finki.emagazine.repository;

import mk.ukim.finki.emagazine.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
