package com.vaibhav.journalApp.scheduler;

import com.vaibhav.journalApp.cache.AppCache;
import com.vaibhav.journalApp.entity.JournalEntry;
import com.vaibhav.journalApp.entity.User;
import com.vaibhav.journalApp.repository.UserRepositoryImpl;
import com.vaibhav.journalApp.service.EmailService;
import com.vaibhav.journalApp.service.SentimentAnalysisService;
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
    @Scheduled(cron = "0 0 9 ? * SUN *")
//    @Scheduled(cron = "0 0/1 * 1/1 * ? *")
    public void fetchUserAndSendEmail() {
        List<User> users =  userRepository.getUserForSA();
        for(User user : users) {
          List<JournalEntry> journalEntries = user.getJournalEntries();

          List<String> filteredEntries =  journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList());

          String entry = String.join(" ", filteredEntries);

          String sentiment = String.valueOf(sentimentAnalysisService.getSentiment(entry));

          emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", sentiment);

        }
    }

    @Scheduled(cron = "0 0/10 * 1/1 * ? *")
    public void clearAppCache(){
        appCache.init();
    }



}
