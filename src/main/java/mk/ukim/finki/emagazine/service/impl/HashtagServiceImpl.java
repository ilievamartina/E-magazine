package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.Hashtag;
import mk.ukim.finki.emagazine.model.Post;
import mk.ukim.finki.emagazine.model.exceptions.PostNotFountException;
import mk.ukim.finki.emagazine.repository.HashtagRepository;
import mk.ukim.finki.emagazine.repository.PostRepository;
import mk.ukim.finki.emagazine.service.HashtagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostRepository postRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository, PostRepository postRepository) {
        this.hashtagRepository = hashtagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Hashtag create(String description) {
        Hashtag h = new Hashtag(description);
        return this.hashtagRepository.save(h);
    }

    @Override
    public void delete(String hashtagId) {
        this.hashtagRepository.deleteById(hashtagId);
    }

    @Override
    public List<Hashtag> findAll() {
        return this.hashtagRepository.findAll();
    }

    @Override
    public List<Hashtag> getTagsPerPost(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(PostNotFountException::new);
        return this.hashtagRepository.findAll().stream().filter(h -> h.getPosts().contains(post))
                .collect(Collectors.toList());
    }
}
