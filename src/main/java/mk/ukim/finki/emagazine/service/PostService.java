package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.Category;
import mk.ukim.finki.emagazine.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<Post> listAll(String cat);

    Post create(String title, String content, Category cat, String hashtag, MultipartFile photoId) throws IOException;

    void delete(Long postId);

    void edit(Long postId, String title, String content, Category category, String hashtags, MultipartFile photo) throws IOException;

    Post findById(Long postId);

    List<Post> findPostsByKeyword(String keyword);

    void like(Long postId);

    void dislike(Long postId);

    List<Post> mostPopular(String cat);

    List<Post> mostViewed();

    List<Post> trending();

    void click(Long id);

    Post getPostByComment(Long commentId);

    Page<Post> findPaginated(Pageable pageable);

    List<Post> getPostsByAuthor(String username);


}
