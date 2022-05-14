package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.Hashtag;

import java.util.List;

public interface HashtagService {

    Hashtag create(String description);

    void delete(String hashtagId);

    List<Hashtag> findAll();

    List<Hashtag> getTagsPerPost(Long postId);
}
