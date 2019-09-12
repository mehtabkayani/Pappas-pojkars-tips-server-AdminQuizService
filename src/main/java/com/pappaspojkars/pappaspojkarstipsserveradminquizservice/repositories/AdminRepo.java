package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Match;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Question;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Quiz;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
public class AdminRepo {


    @Autowired
    private GameRepo game;
    @Autowired
    private MatchRepo match;
    @Autowired
    private QuestionRepo question;
    @Autowired
    private QuizRepo quiz;
    @Autowired
    private TeamRepo team;


    public GameRepo game() {
        return game;
    }

    public QuestionRepo question() {
        return question;
    }

    public QuizRepo quiz() {
        return quiz;
    }

    public TeamRepo team() {
        return team;
    }

    public MatchRepo match(){return match;}
}
