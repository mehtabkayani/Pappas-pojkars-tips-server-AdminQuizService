package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter @Setter
@NoArgsConstructor
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany
    private List<Question> questions;
    private String name;

    @ManyToOne
    private Game game;

    public Quiz(List<Question> questions, String name, Game game) {
        this.name = name;
        this.questions = questions;
        this.game = game;
    }

    public Quiz(String name, Game game) {
        this.name = name;
        this.questions = new ArrayList<>();
        this.game = game;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
