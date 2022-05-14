package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.Comment;
import mk.ukim.finki.emagazine.model.Post;
import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.model.exceptions.CommentNotFoundException;
import mk.ukim.finki.emagazine.model.exceptions.PostNotFountException;
import mk.ukim.finki.emagazine.model.exceptions.UserNotFoundException;
import mk.ukim.finki.emagazine.repository.CommentRepository;
import mk.ukim.finki.emagazine.repository.PostRepository;
import mk.ukim.finki.emagazine.repository.UserRepository;
import mk.ukim.finki.emagazine.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, PostRepository postRepository1) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository1;
    }

    @Override
    public Comment create(String content, Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        Comment comment = new Comment(user, content, post);
        return this.commentRepository.save(comment);
    }

    @Override
    public void like(Long commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.setNumLikes(comment.getNumLikes() + 1);
        this.commentRepository.save(comment);
    }

    @Override
    public void dislike(Long commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.setNumDislikes(comment.getNumDislikes() + 1);
        this.commentRepository.save(comment);
    }

    @Override
    public void delete(Long commentId) {
        this.commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getCommentsPerPost(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(PostNotFountException::new);
        return this.commentRepository.findAll().stream().filter(c -> c.getPost().equals(post))
                .collect(Collectors.toList());
    }
}
