package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.Video;

import java.util.List;

public interface VideoService {

    List<Video> findAllVideos();

    void delete(Long id);

    void saveVideo(String url);

}
