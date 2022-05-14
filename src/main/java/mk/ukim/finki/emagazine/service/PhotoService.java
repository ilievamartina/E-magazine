package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    void save(MultipartFile multipartFile) throws IOException;

    Photo getPhotoByName(String name);

    List<Photo> getAllPhotos();

}
