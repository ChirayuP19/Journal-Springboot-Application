package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll(){
       return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("/id/{myid}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myid ){
        return journalEntryService.findById(myid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id){
//       journalEntryService.deleteById(id);
//       return ResponseEntity.status(HttpStatus.OK).build();
        Optional<JournalEntry> byId = journalEntryService.findById(id);
        if(byId.isPresent()){
           journalEntryService.deleteById(id);
           return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id,@RequestBody JournalEntry entry){
        JournalEntry data = journalEntryService.findById(id).orElse(null);
        if(data!=null){
            data.setTitle(entry.getTitle() != null && entry.getTitle().equals("") ? entry.getTitle(): data.getTitle());
            data.setContent(entry.getContent() != null && entry.getContent().equals(" ")? entry.getContent() : data.getContent());
        }
        journalEntryService.saveEntry(data);
        return data;
}
}
