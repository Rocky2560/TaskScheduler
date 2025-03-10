package com.example.demo.controller;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

//    @Autowired
//    private UserService UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/auth/registration")
    public String register()
    {
        return "registration";
    }

    @PostMapping("auth/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email, RedirectAttributes redirectAttributes) {

        if (userRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("message", "User already registered. Please Login now!");
            return "redirect:/auth/login"; // Handle duplicate user case
        }

        Users user = new Users();
        user.setUsername(username);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "User registered Successful. Please Login now!");
        return "redirect:/auth/login"; // ✅ Redirect to login after successful registration
    }
}
