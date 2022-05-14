package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.service.PostService;
import mk.ukim.finki.emagazine.service.UserService;
import mk.ukim.finki.emagazine.service.VideoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VideoController {

    private final PostService postService;
    private final UserService userService;
    private final VideoService videoService;

    public VideoController(PostService postService, UserService userService, VideoService videoService) {
        this.postService = postService;
        this.userService = userService;
        this.videoService = videoService;
    }

    @GetMapping("/addVideo")
    public String getVideoForm(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostViewed", this.postService.mostViewed());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getName() != "anonymousUser") {
            User user = this.userService.getUserByUsername(auth.getName());
            model.addAttribute("user", user);
        }
        return "addVideoForm";
    }

    @PostMapping("/saveVideo")
    public String saveVideo(@RequestParam String url) {
        this.videoService.saveVideo(url);
        return "redirect:/home";
    }

    @GetMapping("/deleteVideo/{id}")
    public String deleteVideo(@PathVariable Long id) {
        this.videoService.delete(id);
        return "redirect:/home";
    }

}
