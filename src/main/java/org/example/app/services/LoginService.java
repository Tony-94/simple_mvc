package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);

    private final LoginForm users = new LoginForm();

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-form: " + loginForm);
        return loginForm.getUsername().equals(users.getUsername()) && loginForm.getPassword().equals(users.getPassword());
    }

    public LoginForm saveUser(LoginForm loginForm) {
        users.setUsername(loginForm.getUsername());
        users.setPassword(loginForm.getPassword());
        return users;
    }
}
