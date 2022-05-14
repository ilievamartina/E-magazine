package mk.ukim.finki.emagazine.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;


@Entity
@Data
public class Hashtag {

    @Id
    private String description;
    @ManyToMany(mappedBy = "hashtags")
    private List<Post> posts;

    public Hashtag() {

    }

    public Hashtag(String description) {
        this.description = description;
    }
}
