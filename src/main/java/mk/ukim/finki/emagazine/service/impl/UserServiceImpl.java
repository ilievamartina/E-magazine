package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.*;
import mk.ukim.finki.emagazine.model.exceptions.InvalidUsernameOrPasswordException;
import mk.ukim.finki.emagazine.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.emagazine.model.exceptions.UserAlreadyExistsException;
import mk.ukim.finki.emagazine.model.exceptions.UserNotFoundException;
import mk.ukim.finki.emagazine.repository.CommentRepository;
import mk.ukim.finki.emagazine.repository.PhotoRepository;
import mk.ukim.finki.emagazine.repository.PostRepository;
import mk.ukim.finki.emagazine.repository.UserRepository;
import mk.ukim.finki.emagazine.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserServiceImpl(UserRepository userRepository, PhotoRepository photoRepository, PasswordEncoder passwordEncoder, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public User register(String username, String password, String name, String surname, String repeatPassword,
                         String email, LocalDate birthDate) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        Photo photo = this.photoRepository.findByName("user.png");
        User user = new User(username, name, surname, passwordEncoder.encode(password), email, birthDate, Role.ROLE_USER, photo);
        return this.userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void changeProfilePicture(String username, MultipartFile photo) throws IOException {
        Photo photo1 = this.photoRepository.findByName(photo.getOriginalFilename());
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        user.setProfilePic(photo1);
        this.userRepository.save(user);
    }

    @Override
    public void deleteAccount(String username) {
        Photo profilePic = this.userRepository.findByUsername(username).get().getProfilePic();
        this.photoRepository.delete(profilePic);
        List<Post> posts = this.postRepository.findAll().stream()
                .filter(p -> p.getAuthor().getUsername().equals(username))
                .collect(Collectors.toList());
        this.postRepository.deleteAll(posts);
        List<Comment> comments = this.commentRepository.findAll().stream()
                .filter(c -> c.getAuthor().getUsername().equals(username))
                .collect(Collectors.toList());
        this.commentRepository.deleteAll(comments);
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        this.userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
