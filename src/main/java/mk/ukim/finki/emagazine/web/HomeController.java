package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.Post;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.service.HashtagService;
import mk.ukim.finki.emagazine.service.PostService;
import mk.ukim.finki.emagazine.service.UserService;
import mk.ukim.finki.emagazine.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping({"/", "/home"})
public class HomeController {

    private final PostService postService;
    private final HashtagService hashtagService;
    private final VideoService videoService;
    private final UserService userService;

    public HomeController(PostService postService, HashtagService hashtagService, VideoService videoService,
                          UserService userService) {
        this.postService = postService;
        this.hashtagService = hashtagService;
        this.videoService = videoService;
        this.userService = userService;
    }


    @GetMapping
    public String getHomePage(Model model, @RequestParam Optional<Integer> page) {

        int currentPage = page.orElse(1);
        int pageSize = 4;

        Page<Post> postPage = this.postService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("postPage", postPage);

        int totalPages = postPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", currentPage);
        }

        model.addAttribute("posts", this.postService.listAll(null));
        model.addAttribute("hashtags", this.hashtagService.findAll().subList(0, 15));
        model.addAttribute("categories", Category.values());
        model.addAttribute("videos", this.videoService.findAllVideos());
        model.addAttribute("trending", this.postService.trending());
        model.addAttribute("mostPopular", this.postService.mostPopular(null));
        model.addAttribute("mostViewed", this.postService.mostViewed());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }

        return "home";
    }

    @GetMapping("/filtered")
    public String filtered(Model model, @RequestParam String keyword) {
        model.addAttribute("filtered", this.postService.findPostsByKeyword(keyword));
        model.addAttribute("mostViewed", this.postService.mostViewed());
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostPopular", this.postService.mostPopular(null));
        model.addAttribute("hashtags", this.hashtagService.findAll().subList(0, 15));
        return "filtered";
    }


}
