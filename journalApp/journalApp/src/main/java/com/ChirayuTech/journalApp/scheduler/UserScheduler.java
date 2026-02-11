package com.ChirayuTech.journalApp.scheduler;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.repository.UserRepositoryImpl;
import com.ChirayuTech.journalApp.service.EmailService;
import com.ChirayuTech.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSaMail(){
        List<User> users = userRepository.getUsersForSA();
        for(User user:users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> filteredEntries = journalEntries.stream()
                    .filter(x -> x.getLocalDateTime()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getContent())
                    .collect(Collectors.toList());
            String entry = String.join(" ", filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail("sonu010675@gmail.com","Semtiment for last 7 days",sentiment);
        }
    }
}
