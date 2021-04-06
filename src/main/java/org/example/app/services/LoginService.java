package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);

    private final List<User> users = new ArrayList<>();

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-form: " + loginForm);
        return users.stream().findFirst().get().getUsername().equals(loginForm.getUsername())
                && users.stream().findFirst().get().getPassword().equals(loginForm.getPassword());
    }

    public List<User> saveUser(User user) {
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
            users.forEach(u -> {
                u.setUsername(user.getUsername());
                u.setPassword(user.getPassword());
                users.add(u);
            });
        }
        return users;
    }
}
