package com.ChirayuTech.journalApp.service;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.repository.JournalEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }


}
