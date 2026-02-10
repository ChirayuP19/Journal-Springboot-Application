package com.ChirayuTech.journalApp.service;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

   private static final Logger logger=LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String byUserName){
        try {
            User entry = userService.findByUserName(byUserName);
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            entry.getJournalEntries().add(saved);
            userService.saveUser(entry);
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry ",e);
        }
    }

    public JournalEntry saveEntry(JournalEntry journalEntry){
        return journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
       return journalEntryRepo.findAll();
    }


    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id,String username){
        boolean removed =false;
        try {
            User userName = userService.findByUserName(username);
            removed = userName.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveUser(userName);
                journalEntryRepo.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the entry. ",e);
        }
        return removed;
    }



}
