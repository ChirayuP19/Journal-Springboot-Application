package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try {
            User entry = userService.saveEntry(user);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAll();
        if(users !=null && !users.isEmpty()){
            return new ResponseEntity<>(users,HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id){
        Optional<User> user = userService.findById(id);
        return user.map(user1 -> new ResponseEntity<>(user1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @DeleteMapping("/id/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable ObjectId id){
//        userService.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @PutMapping("/id/{username}")
    public ResponseEntity<?> updateUserById(@RequestBody User user, @PathVariable String username){
        User userName = userService.findByUserName(username);
        if(userName!=null){
            userName.setUsername(user.getUsername());
            userName.setPassword(user.getPassword());
            User entry = userService.saveEntry(userName);
            return new ResponseEntity<>(entry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
