package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.*;
import mk.ukim.finki.emagazine.model.exceptions.CommentNotFoundException;
import mk.ukim.finki.emagazine.model.exceptions.PostNotFountException;
import mk.ukim.finki.emagazine.model.exceptions.UserNotFoundException;
import mk.ukim.finki.emagazine.repository.*;
import mk.ukim.finki.emagazine.service.PhotoService;
import mk.ukim.finki.emagazine.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;


    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository,
                           UserRepository userRepository, HashtagRepository hashtagRepository, PhotoService photoService, PhotoRepository photoRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
        this.photoService = photoService;
        this.photoRepository = photoRepository;
    }


    @Override
    public List<Post> listAll(String cat) {
        List<Post> posts = new ArrayList<>();
        if (cat == null) {
            posts = this.postRepository.findAll()
                    .stream().sorted(Comparator.comparing(Post::getDateCreated).reversed())
                    .collect(Collectors.toList());
        } else {
            posts = this.postRepository.findAll().stream().filter(p -> p.getCategory().name().toLowerCase().equals(cat))
                    .sorted(Comparator.comparing(Post::getDateCreated).reversed())
                    .collect(Collectors.toList());
        }

        return posts;
    }


    @Override
    public Post create(String title, String content, Category cat, String hashtags,
                       MultipartFile photo) throws IOException {

        List<Hashtag> tags = new ArrayList<>();
        if (hashtags != null) {
            List<String> tagsStr = Arrays.asList(hashtags.split(" "));
            tagsStr.forEach(t -> {
                this.hashtagRepository.save(new Hashtag(t));
                tags.add(new Hashtag(t));
            });
        }
        this.photoService.save(photo);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        Photo photo1 = this.photoRepository.findByName(photo.getOriginalFilename());
        Post post = new Post(title, content, LocalDate.now(), user, cat, tags, photo1);
        return this.postRepository.save(post);
    }

    @Override
    public void delete(Long postId) {
        Post post = this.postRepository.getById(postId);
        this.photoRepository.delete(post.getPhoto());
        this.postRepository.deleteById(postId);
    }

    @Override
    public void edit(Long postId, String title, String content, Category category,
                     String hashtags, MultipartFile photo) throws IOException {

        Post post = this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
        if (hashtags != null) {
            List<String> tags = Arrays.asList(hashtags.split(" "));
            List<Hashtag> tagsPerPost = post.getHashtags();
            tags.forEach(t -> {
                this.hashtagRepository.save(new Hashtag(t));
                tagsPerPost.add(new Hashtag(t));
            });
        }
        post.setTitle(title);
        post.setContent(content);
        post.setLastEdit(LocalDate.now());
        post.setCategory(category);

        if (photo.getSize() == 0) {
            post.setPhoto(post.getPhoto());
        } else {
            this.photoService.save(photo);
            Photo photo1 = this.photoRepository.findByName(photo.getOriginalFilename());
            post.setPhoto(photo1);
        }
        this.postRepository.save(post);
    }

    @Override
    public Post findById(Long postId) {
        return this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
    }

    @Override
    public List<Post> findPostsByKeyword(String keyword) {
        return this.postRepository.findAll().stream().filter(p -> p.getTitle().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void like(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
        post.setNumLikes(post.getNumLikes() + 1);
        this.postRepository.save(post);
    }

    @Override
    public void dislike(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
        post.setNumDislikes(post.getNumDislikes() + 1);
        this.postRepository.save(post);
    }


    @Override
    public List<Post> mostPopular(String cat) {

        List<Post> posts = new ArrayList<>();
        if (cat == null) {
            posts = this.postRepository.findAll().stream().sorted(Comparator.comparing(Post::getNumLikes).reversed())
                    .collect(Collectors.toList()).subList(0, 4);
        } else {
            posts = this.postRepository.findAll().stream().filter(p -> p.getCategory().name().toLowerCase().equals(cat))
                    .sorted(Comparator.comparing(Post::getNumLikes).reversed())
                    .collect(Collectors.toList());
        }

        return posts;
    }

    @Override
    public List<Post> mostViewed() {
        return this.postRepository.findAll().stream().sorted(Comparator.comparing(Post::getClicks).reversed())
                .collect(Collectors.toList()).subList(0, 3);
    }

    @Override
    public List<Post> trending() {

        return this.postRepository.findAll().stream().filter(p -> p.getLastEdit().isAfter(LocalDate.now().minusMonths(1)))
                .sorted(Comparator.comparing(Post::getClicks).reversed())
                .collect(Collectors.toList());
    }


    @Override
    public void click(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(PostNotFountException::new);
        post.setClicks(post.getClicks() + 1);
        this.postRepository.save(post);
    }

    @Override
    public Post getPostByComment(Long commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        return comment.getPost();
    }

    @Override
    public Page<Post> findPaginated(Pageable pageable) {
        List<Post> posts = this.postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getLastEdit).reversed())
                .collect(Collectors.toList());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> postList;

        if (posts.size() < startItem) {
            postList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            postList = posts.subList(startItem, toIndex);
        }
        return new PageImpl<>(postList, PageRequest.of(currentPage, pageSize), posts.size());
    }

    @Override
    public List<Post> getPostsByAuthor(String username) {
        return this.postRepository.findAll()
                .stream().filter(p -> p.getAuthor().getUsername().equals(username))
                .collect(Collectors.toList());
    }


}
