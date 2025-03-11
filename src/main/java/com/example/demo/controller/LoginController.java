package com.example.demo.controller;

import com.example.demo.JWT.JwtUtil;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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

//    @PostMapping("auth/register")
//    public String register(@RequestParam("username") String username,
//                           @RequestParam("password") String password,
//                           @RequestParam("email") String email, RedirectAttributes redirectAttributes) {
//
//        if (userRepository.findByUsername(username).isPresent()) {
//            redirectAttributes.addFlashAttribute("message", "User already registered. Please Login now!");
//            return "redirect:/auth/login"; // Handle duplicate user case
//        }
//
//        Users user = new Users();
//        user.setUsername(username);
//        user.setUsername(email);
//        user.setPassword(passwordEncoder.encode(password));
//        userRepository.save(user);
//        redirectAttributes.addFlashAttribute("message", "User registered Successful. Please Login now!");
//        return "redirect:/auth/login"; // ✅ Redirect to login after successful registration
//    }
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Authenticate user
        if (isAuthenticated(username, password)) {
            String token = jwtUtil.generateToken(username);

            // Create response with the token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("redirect", "/tasks");  // or any page where you want to redirect

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Simple authentication check (replace this with actual authentication logic)
    private boolean isAuthenticated(String username, String password) {
        // Here you would normally check the username and password against a database or another source
        return "user".equals(username) && "password".equals(password);
    }


//    @PostMapping("auth/login")
//    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
//        System.out.println("hello");
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtUtil.generateToken(username);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        response.put("redirect", "/tasks");
//        return response;
//    }

//    @PostMapping("/auth/login")
//    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
//        // Your authentication logic here
//
//        // If authentication is successful
//        if ((username, password)) {
//            // Store any message or token if needed
//            redirectAttributes.addFlashAttribute("message", "Login successful!");
//            return "redirect:/tasks";  // Redirect to tasks page
//        } else {
//            // If authentication fails, redirect back to the login page with an error
//            redirectAttributes.addAttribute("error", true);
//            return "redirect:/auth/login";
//        }
//
//    }
}
