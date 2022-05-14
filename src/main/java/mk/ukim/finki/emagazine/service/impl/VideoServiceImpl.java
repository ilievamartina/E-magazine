package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.Video;
import mk.ukim.finki.emagazine.repository.VideoRepository;
import mk.ukim.finki.emagazine.service.VideoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }


    @Override
    public List<Video> findAllVideos() {
        return this.videoRepository.findAll()
                .stream().sorted(Comparator.comparing(Video::getDateCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        this.videoRepository.deleteById(id);
    }

    @Override
    public void saveVideo(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.youtube.com/embed/");
        sb.append(url.substring(32, 43));
        sb.append("?");
        Video video = new Video(sb.toString(), LocalDate.now());
        this.videoRepository.save(video);
    }
}
