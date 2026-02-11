package com.ChirayuTech.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Value("${TO}")
    private String to;

    @Test
     void testSendMail(){
         emailService.sendEmail(to,
                 "Testing Java mail sender",
                 "Hii this is send form Spring-Boot journal App");
     }
}
