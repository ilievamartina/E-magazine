package mk.ukim.finki.emagazine.web;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.emagazine.service.AuthService;
import mk.ukim.finki.emagazine.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    private final PostService postService;

    public LoginController(AuthService authService, PostService postService) {
        this.authService = authService;
        this.postService = postService;
    }

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("mostViewed", this.postService.mostViewed());
        model.addAttribute("categories", Category.values());
        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        User user = null;
        try {
            user = this.authService.login(request.getParameter("username"),
                    request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/home";
        } catch (InvalidUserCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "login";
        }
    }

}
