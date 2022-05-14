package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Post;
import mk.ukim.finki.emagazine.service.CommentService;
import mk.ukim.finki.emagazine.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/addComment/{id}")
    public String commentPost(@RequestParam String comment, @PathVariable Long id) {
        this.commentService.create(comment, id);
        return "redirect:/posts/{id}";
    }

    @GetMapping("/comment/like/{id}")
    public String likeComment(@PathVariable Long id) {
        Post post = this.postService.getPostByComment(id);
        this.commentService.like(id);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/comment/dislike/{id}")
    public String dislikeComment(@PathVariable Long id) {
        Post post = this.postService.getPostByComment(id);
        this.commentService.dislike(id);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        Post post = this.postService.getPostByComment(id);
        this.commentService.delete(id);
        return "redirect:/posts/" + post.getId();
    }
}
