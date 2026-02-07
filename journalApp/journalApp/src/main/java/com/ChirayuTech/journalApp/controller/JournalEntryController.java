package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll(){
       return null;
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry){
        journalEntryService.saveEntry(myEntry);
        return true;
    }

    @GetMapping("/id/{myid}")
    public JournalEntry getJournalEntryById(@PathVariable Long myid ){
        return null;
    }

    @DeleteMapping("/id/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long id){
        return null;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable Long id,@RequestBody JournalEntry entry){
        return null;
}
}
