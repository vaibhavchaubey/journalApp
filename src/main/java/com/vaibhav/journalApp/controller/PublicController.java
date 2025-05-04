package com.vaibhav.journalApp.controller;

import com.vaibhav.journalApp.entity.User;
import com.vaibhav.journalApp.service.UserDetailsServiceImpl;
import com.vaibhav.journalApp.service.UserService;
import com.vaibhav.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userService.saveNewUser(user);
    }


    /* Flow of verify(Users user):
     When you call authManager.authenticate(...), here’s what happens behind the scenes:

    1) Spring’s AuthenticationManager is used to handle the authentication.

    2) AuthenticationManager loops through its list of AuthenticationProviders.

    3) It finds your DaoAuthenticationProvider because you registered it as a @Bean.

    4) DaoAuthenticationProvider:
        Calls your UserDetailsServiceImpl to fetch the user by username.

        Uses the PasswordEncoder to compare the provided raw password with the hashed password in DB.

    5) If all checks pass, Spring returns an Authentication object that is authenticated.  */

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            UserDetails userDetails =  userDetailsService.loadUserByUsername(user.getUsername());

            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch(Exception e){
           log.error("Exception occurred while createAuthenticationToken ", e);
           return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }


}
