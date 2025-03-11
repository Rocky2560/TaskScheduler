//package com.example.demo.controller;
//
//import com.example.demo.JWT.JwtUtil;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//    @RestController
//    @RequestMapping("/auth")
//    public class AuthController {
//
//        @Autowired
//        private AuthenticationManager authenticationManager;
//
//        @Autowired
//        private JwtUtil jwtUtil;
//
//        @PostMapping("/login")
//        public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
//            System.out.println("hello");
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String token = jwtUtil.generateToken(username);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("token", token);
//            response.put("redirect", "/tasks");
//            return response;
//        }
//
//
//
//
//}
