package mk.ukim.finki.emagazine.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastEdit;
    @ManyToOne
    @NotNull
    private User author;
    private Integer numLikes;
    private Integer numDislikes;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @ManyToMany
    @JoinTable(
            name = "post_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private List<Hashtag> hashtags;
    private int clicks;
    @OneToOne(cascade = CascadeType.ALL)
    private Photo photo;


    public Post(@NotEmpty String title, @NotEmpty String content, LocalDate dateCreated, User author, Category category,
                List<Hashtag> hashtags, Photo photo) {
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
        this.lastEdit = dateCreated;
        this.author = author;
        this.numLikes = 0;
        this.numDislikes = 0;
        this.category = category;
        this.hashtags = hashtags;
        this.clicks = 0;
        this.photo = photo;

    }

    public Post() {
    }

}
