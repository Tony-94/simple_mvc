package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class LoginServiceImpl implements LoginService {

    private Logger logger = Logger.getLogger(LoginServiceImpl.class);

    private final List<User> users = new ArrayList<>();

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-form: " + loginForm);
        for (User user : users) {
            if (user.getUsername().equals(loginForm.getUsername()) &&
                    user.getPassword().equals(loginForm.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public List<User> saveUser(User user) {
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
            users.add(user);
        }
        return users;
    }
}
