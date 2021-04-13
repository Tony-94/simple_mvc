package org.example.app.services;

import org.example.web.dto.LoginForm;
import org.example.web.dto.User;

import java.util.List;

public interface LoginService {

   boolean authenticate(LoginForm loginForm);

    List<User> saveUser(User user);
}
