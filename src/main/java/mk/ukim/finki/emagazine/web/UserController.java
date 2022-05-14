package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.service.PhotoService;
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

@Controller
public class UserController {

    private final PostService postService;
    private final PhotoService photoService;
    private final UserService userService;

    public UserController(PostService postService, PhotoService photoService, UserService userService) {
        this.postService = postService;
        this.photoService = photoService;
        this.userService = userService;
    }

    @GetMapping("/profile/{username}")
    public String getProfile(@PathVariable String username, Model model) {

        User user = this.userService.getUserByUsername(username);
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostPopular", this.postService.mostPopular(null));
        model.addAttribute("userProfile", user);
        model.addAttribute("usersPosts", this.postService.getPostsByAuthor(username));
        return "profile";
    }

    @PostMapping("/changeProfilePic")
    public String changeProfilePicture(@RequestParam MultipartFile photo) throws IOException {
        this.photoService.save(photo);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.userService.changeProfilePicture(auth.getName(), photo);
        return "redirect:/profile/" + auth.getName();
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String username) {
        this.userService.deleteAccount(username);
        return "redirect:/login";
    }
}
