package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

//    @PostMapping("")
//    public ResponseEntity<?> createUser(@RequestBody User user){
//        try {
//            User entry = userService.saveEntry(user);
//            return new ResponseEntity<>(entry, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

//    @GetMapping("/id/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable ObjectId id){
//        Optional<User> user = userService.findById(id);
//        return user.map(user1 -> new ResponseEntity<>(user1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }


    @PutMapping("")
    public ResponseEntity<?> updateUserById(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User userName = userService.findByUserName(name);
        userName.setUsername(user.getUsername());
        userName.setPassword(user.getPassword());
        userService.saveNewEntry(userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUserByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User byUserName = userService.findByUserName(name);
        if(byUserName!=null){
            userService.deleteByUsername(name);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?>greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>("hi "+authentication.getName(),HttpStatus.OK);
    }

}
