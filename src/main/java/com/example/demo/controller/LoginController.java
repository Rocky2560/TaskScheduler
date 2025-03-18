package com.example.demo.controller;

import com.example.demo.JWT.JwtUtil;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Users;
import com.example.demo.service.UserDetailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("/login")
    public String login()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/tasks";
        }
        return "login"; // your login view name
    }

    @GetMapping("/registration")
    public String register()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/tasks";
        }
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
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        System.out.println("hello");
        redirectAttributes.addFlashAttribute("message", "User registered Successful. Please Login now!");
        return "redirect:/login"; // ✅ Redirect to login after successful registration
    }

    @PostMapping("login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        var userOpt = userDetailService.authenticate(username, password);
        System.out.println("hello");
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
//            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
//            session.setAttribute("token", token);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            System.out.println(user.getRole());
            return user.getRole().equals("user") ? "redirect:/tasks" : "redirect:/tasks";
        } else {
            model.addAttribute("message", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("username", session.getAttribute("username"));
        return "admin-dashboard";
    }

//    @PostMapping("/auth/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//
//        // Authenticate user
//        if (isAuthenticated(username, password)) {
//            String token = jwtUtil.generateToken(username);
//
//            // Create response with the token
//            Map<String, String> response = new HashMap<>();
//            response.put("token", token);
//            response.put("redirect", "/tasks");  // or any page where you want to redirect
//
//            return ResponseEntity.ok(response);
//        } else {
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Invalid credentials");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }

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
