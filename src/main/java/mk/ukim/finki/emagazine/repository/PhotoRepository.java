package mk.ukim.finki.emagazine.repository;

import mk.ukim.finki.emagazine.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
    Photo findByName(String name);
}
