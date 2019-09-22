package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime timeStarted;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime timeLockedDown;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime timeEnded;

    @OneToMany
    private List<Quiz> quizes;


    public void addQuiz(Quiz quiz) {
        quizes.add(quiz);
    }

    public Game() {

    }

    public Game(String name, List<Quiz> quizes, LocalDateTime timeLockedDown) {
        this.name = name;
        this.quizes = quizes;
        this.timeLockedDown = timeLockedDown;

        this.timeStarted = LocalDateTime.now();
    }

    public Game(String name, LocalDateTime timeLockedDown) {
        this.name = name;
        this.quizes = new ArrayList<>();
        this.timeLockedDown = timeLockedDown;

        this.timeStarted = LocalDateTime.now();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(LocalDateTime timeStarted) {
        this.timeStarted = timeStarted;
    }

    public LocalDateTime getTimeLockedDown() {
        return timeLockedDown;
    }

    public void setTimeLockedDown(LocalDateTime timeLockedDown) {
        this.timeLockedDown = timeLockedDown;
    }

    public LocalDateTime getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(LocalDateTime timeEnded) {
        this.timeEnded = timeEnded;
    }

    public List<Quiz> getQuizes() {
        return quizes;
    }

    public void setQuizes(List<Quiz> quizes) {
        this.quizes = quizes;
    }
}
