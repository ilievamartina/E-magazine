package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.Comment;

import java.util.List;

public interface CommentService {

    Comment create(String content, Long postId);

    void like(Long commentId);

    void dislike(Long commentId);

    void delete(Long commentId);

    List<Comment> getCommentsPerPost(Long id);
}
