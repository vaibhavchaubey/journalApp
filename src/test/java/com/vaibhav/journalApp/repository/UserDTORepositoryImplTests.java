package com.vaibhav.journalApp.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDTORepositoryImplTests {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    public void TestGetUserForSA(){
        Assertions.assertNotNull(userRepository.getUserForSA());

    }


}
