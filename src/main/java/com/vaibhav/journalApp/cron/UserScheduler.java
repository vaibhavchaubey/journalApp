package com.vaibhav.journalApp.cron;

import com.vaibhav.journalApp.cache.AppCache;
import com.vaibhav.journalApp.entity.JournalEntry;
import com.vaibhav.journalApp.entity.User;
import com.vaibhav.journalApp.enums.Sentiment;
import com.vaibhav.journalApp.repository.UserRepositoryImpl;
import com.vaibhav.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AppCache appCache;

    /*
    0 0 9 ? * SUN *

    0 → Seconds: At 0 seconds.

    0 → Minutes: At 0 minutes.

    9 → Hours: At 9 AM.

    ? → Day-of-Month: No specific value (used when day-of-week is specified).

    * → Month: Every month.

    SUN → Day-of-Week: Only on Sunday.

    * → Year: Every year.
    * */
//    @Scheduled(cron = "0 0 9 ? * SUN *")
//    @Scheduled(cron = "0 0/1 * 1/1 * ? *")

    /**
     * Fetches users from the repository, analyzes their journal entries from the last 7 days,
     * determines the most frequent sentiment, and sends an email to each user with their dominant sentiment.
     */
    public void fetchUserAndSendEmail() {
        List<User> users =  userRepository.getUserForSA();
        for(User user : users) {
          List<JournalEntry> journalEntries = user.getJournalEntries();

            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days ", mostFrequentSentiment.toString());
            }
        }
    }

//    @Scheduled(cron = "0 0/10 * 1/1 * ? *")
    public void clearAppCache(){
        appCache.init();
    }



}
