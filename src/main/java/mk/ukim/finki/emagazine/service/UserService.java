package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface UserService extends UserDetailsService {

    User register(String username, String password, String repeatPassword, String name, String surname,
                  String email, LocalDate birthDate);

    User getUserByUsername(String username);

    void changeProfilePicture(String username, MultipartFile photo) throws IOException;

    void deleteAccount(String username);


}
