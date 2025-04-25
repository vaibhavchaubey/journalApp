package com.vaibhav.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail(){
        emailService.sendEmail("vaibhavchaubey8802@gmail.com", "Test java mail sender 2", "This is a test mail from java mail sender 2");
    }
}
