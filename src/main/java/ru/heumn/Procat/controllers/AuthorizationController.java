package ru.heumn.Procat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.heumn.Procat.storage.entities.UserEntity;
import ru.heumn.Procat.storage.repository.UserRepository;

@RestController
@RequestMapping("/login")
public class AuthorizationController {

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public String login(@RequestParam(required = false) String error, Model model) {

        try {
            if (error.isEmpty()) {
                error = "Неправильный логин или пароль";
            }
        } catch (Exception ignored) {
        }

        model.addAttribute("userError", error);
        return "login";
    }
}
