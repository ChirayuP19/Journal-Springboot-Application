package com.ChirayuTech.journalApp.entity;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data

public class JournalEntry {

    private long id;
    private String title;
    private String content;
}
