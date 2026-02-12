package com.ChirayuTech.journalApp.scheduler;

import com.ChirayuTech.journalApp.emuns.Sentiment;
import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.model.SentimentData;
import com.ChirayuTech.journalApp.repository.UserRepositoryImpl;
import com.ChirayuTech.journalApp.service.EmailService;
import com.ChirayuTech.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Value("${TO}")
//    private String to;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSaMail(){
        List<User> users = userRepository.getUsersForSA();
        for(User user:users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getLocalDateTime()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment())
                    .filter(x -> x != null)
                    .collect(Collectors.toList());

            Map<Sentiment,Integer>sentimentCounts=new HashMap<>();
            for (Sentiment sentiment: sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }

                Sentiment mostFrequentSentiment = null;
                int maxCount= 0;
                for(Map.Entry<Sentiment,Integer> entry:sentimentCounts.entrySet()){
                    if(entry.getValue()> maxCount){
                        maxCount=entry.getValue();
                        mostFrequentSentiment=entry.getKey();
                    }
                }
                if(mostFrequentSentiment !=null){
                    SentimentData sentimentData=SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last for last 7 days"+mostFrequentSentiment).build();
                    kafkaTemplate.send("weekly-sentiments",sentimentData.getEmail(),sentimentData);
                }
        }
    }
}

