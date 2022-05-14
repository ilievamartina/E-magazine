package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.Hashtag;
import mk.ukim.finki.emagazine.model.Post;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.service.CommentService;
import mk.ukim.finki.emagazine.service.HashtagService;
import mk.ukim.finki.emagazine.service.PostService;
import mk.ukim.finki.emagazine.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostsController {

    private final PostService postService;
    private final CommentService commentService;
    private final HashtagService hashtagService;
    private final UserService userService;


    public PostsController(PostService postService, CommentService commentService, HashtagService hashtagService,
                           UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.hashtagService = hashtagService;
        this.userService = userService;
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        this.postService.click(id);
        model.addAttribute("openedPost", this.postService.findById(id));
        model.addAttribute("tags", this.hashtagService.getTagsPerPost(id));
        model.addAttribute("mostPopular", this.postService.mostPopular(null));
        model.addAttribute("trending", this.postService.trending());
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostViewed", this.postService.mostViewed());
        model.addAttribute("comments", this.commentService.getCommentsPerPost(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }
        return "post";
    }

    @GetMapping("/category/{cat}")
    public String getPostsPerCategory(@PathVariable String cat, Model model) {
        model.addAttribute("postsPerCategory", this.postService.listAll(cat));
        model.addAttribute("categories", Category.values());
        model.addAttribute("trending", this.postService.trending());
        model.addAttribute("mostPopular", this.postService.mostPopular(cat));
        model.addAttribute("mostViewed", this.postService.mostViewed());
        List<Hashtag> tags = new ArrayList<>();
        this.postService.listAll(cat).forEach(p -> {
            tags.addAll(p.getHashtags());
        });
        model.addAttribute("hashtags", tags);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }
        return "category";
    }

    @GetMapping("/post/like/{id}")
    public String likePost(@PathVariable Long id) {
        this.postService.like(id);
        return "redirect:/posts/{id}";
    }

    @GetMapping("/post/dislike/{id}")
    public String dislikePost(@PathVariable Long id) {
        this.postService.dislike(id);
        return "redirect:/posts/{id}";
    }

    @GetMapping("/post/edit/{id}")
    public String getEditPage(@PathVariable Long id, Model model) {
        Post post = this.postService.findById(id);
        model.addAttribute("categories", Category.values());
        model.addAttribute("postForEdit", post);
        model.addAttribute("mostViewed", this.postService.mostViewed());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }
        return "form";
    }

    @GetMapping("/post/create")
    public String getCreatePage(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostViewed", this.postService.mostViewed());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }
        return "form";
    }

    @PostMapping("/post/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam String content,
                       @RequestParam Category category,
                       @RequestParam(required = false) String hashtag,
                       @RequestParam(required = false) MultipartFile photo) throws IOException {

        this.postService.edit(id, title, content, category, hashtag, photo);
        return "redirect:/posts/{id}";
    }

    @PostMapping("/post")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         @RequestParam Category category,
                         @RequestParam(required = false) String hashtag,
                         @RequestParam MultipartFile photo) throws IOException {

        this.postService.create(title, content, category, hashtag, photo);
        return "redirect:/home";
    }

    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.postService.delete(id);
        return "redirect:/home";
    }


}
