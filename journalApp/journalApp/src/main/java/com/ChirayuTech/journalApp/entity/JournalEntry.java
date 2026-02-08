package com.ChirayuTech.journalApp.entity;


import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Document

public class JournalEntry {

    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime localDateTime=LocalDateTime.now();
}
