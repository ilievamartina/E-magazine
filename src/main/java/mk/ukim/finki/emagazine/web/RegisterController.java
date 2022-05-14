package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.emagazine.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.emagazine.service.AuthService;
import mk.ukim.finki.emagazine.service.PostService;
import mk.ukim.finki.emagazine.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AuthService authService;
    private final UserService userService;
    private final PostService postService;

    public RegisterController(AuthService authService, UserService userService, PostService postService) {
        this.authService = authService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("categories", Category.values());
        model.addAttribute("mostViewed", this.postService.mostViewed());
        return "register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String newPassword,
                           @RequestParam String firstname,
                           @RequestParam String lastname,
                           @RequestParam String repeatPassword,
                           @RequestParam String email,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate) {
        try {
            this.userService.register(username, newPassword, firstname, lastname, repeatPassword, email, birthDate);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }


}
