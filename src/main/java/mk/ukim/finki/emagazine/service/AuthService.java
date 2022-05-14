package mk.ukim.finki.emagazine.service;

import mk.ukim.finki.emagazine.model.User;

public interface AuthService {
    User login(String username, String password);
}
