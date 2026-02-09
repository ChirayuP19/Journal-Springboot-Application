package com.ChirayuTech.journalApp.service;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
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

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String byUserName){
        try {
            User entry = userService.findByUserName(byUserName);
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            entry.getJournalEntries().add(saved);
            userService.saveEntry(entry);
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

    public void deleteById(ObjectId id,String username){
        User userName = userService.findByUserName(username);
        userName.getJournalEntries().removeIf(x->x.getId().equals(id));
        userService.saveEntry(userName);
        journalEntryRepo.deleteById(id);
    }


}
