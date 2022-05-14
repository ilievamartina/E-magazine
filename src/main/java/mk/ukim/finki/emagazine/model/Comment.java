package mk.ukim.finki.emagazine.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "post_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @NotNull
    private User author;
    @NotEmpty
    private String content;
    private Integer numLikes;
    private Integer numDislikes;
    @ManyToOne
    private Post post;

    public Comment(User author, String content, Post post) {
        this.author = author;
        this.content = content;
        this.numLikes = 0;
        this.numDislikes = 0;
        this.post = post;
    }

    public Comment() {
    }
}
