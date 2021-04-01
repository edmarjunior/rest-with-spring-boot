package com.example.udemy.controllers;

import com.example.udemy.repositories.UserRepository;
import com.example.udemy.security.AccountCredentialsVO;
import com.example.udemy.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @PostMapping(value = "/signIn")
    public ResponseEntity signIn(@RequestBody AccountCredentialsVO credentials) {
        try {
            var userName = credentials.getUserName();
            var password = credentials.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            var user = repository.findByUserName(userName);
            var token = "";

            if (user != null) {
                token = tokenProvider.createToken(userName, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + userName + " not found!");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", userName);
            model.put("token", token);

            return ResponseEntity.ok(model);

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("invalid username/password supplied!");
        }
    }



}
