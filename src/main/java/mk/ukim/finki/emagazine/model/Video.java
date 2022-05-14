package mk.ukim.finki.emagazine.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String link;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;

    public Video(String link, LocalDate dateCreated) {
        this.link = link;
        this.dateCreated = dateCreated;
    }

    public Video() {
    }
}
