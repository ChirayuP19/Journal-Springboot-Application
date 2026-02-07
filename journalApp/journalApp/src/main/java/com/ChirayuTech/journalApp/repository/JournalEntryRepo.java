package com.ChirayuTech.journalApp.repository;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepo extends MongoRepository<JournalEntry,String> {
}
