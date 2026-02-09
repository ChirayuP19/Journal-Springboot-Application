package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.JournalEntry;
import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.service.JournalEntryService;
import com.ChirayuTech.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = userService.findByUserName(name);
        List<JournalEntry> all = user.getJournalEntries();
        if(all !=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            JournalEntry entry = journalEntryService.saveEntry(myEntry,username);
            return ResponseEntity.status(HttpStatus.CREATED).body(entry);
        } catch (Exception e) {

           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/id/{myid}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myid ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries()
                .stream().filter(x -> x.getId().equals(myid))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> id = journalEntryService.findById(myid);
            return id.map(journalEntry -> new ResponseEntity<>(journalEntry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(id, username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId id,
                                                               @RequestBody JournalEntry entry){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry data = journalEntry.get();
                data.setTitle(entry.getTitle() != null && ! entry.getTitle().equals("") ? entry.getTitle(): data.getTitle());
                data.setContent(entry.getContent() != null && ! entry.getContent().equals(" ")? entry.getContent() : data.getContent());
                journalEntryService.saveEntry(data);
                return new ResponseEntity<>(data,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
