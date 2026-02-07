package com.ChirayuTech.journalApp.entity;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Document(collation = "journalEntry")
public class JournalEntry {

    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime localDateTime=LocalDateTime.now();
}
