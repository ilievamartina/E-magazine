package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.Photo;
import mk.ukim.finki.emagazine.repository.PhotoRepository;
import mk.ukim.finki.emagazine.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public void save(MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setName(file.getOriginalFilename());
        photo.setData(file.getBytes());
        photo.setSize(file.getSize());
        String imageBase64 = Base64.getEncoder().encodeToString(photo.getData());
        photo.setBase64(imageBase64);
        this.photoRepository.save(photo);

    }

    @Transactional
    @Override
    public Photo getPhotoByName(String name) {
        return this.photoRepository.findByName(name);
    }


    @Override
    public List<Photo> getAllPhotos() {
        return this.photoRepository.findAll();

    }

}
