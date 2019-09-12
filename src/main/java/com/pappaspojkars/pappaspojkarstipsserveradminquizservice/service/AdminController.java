package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.service;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Game;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
   private AdminRepo repo;

    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game game){
        return repo.game().save(game);
    }
}
