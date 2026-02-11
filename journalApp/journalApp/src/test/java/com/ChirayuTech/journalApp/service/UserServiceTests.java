package com.ChirayuTech.journalApp.service;

import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Disabled
    public void tesFindByUsername(){
        User username = userRepository.findByUsername("kavya");
        assertTrue(!username.getJournalEntries().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "3,3,9"
    })
    public void test(int a, int b , int expected){
        assertEquals(expected,a + b);
    }
}
