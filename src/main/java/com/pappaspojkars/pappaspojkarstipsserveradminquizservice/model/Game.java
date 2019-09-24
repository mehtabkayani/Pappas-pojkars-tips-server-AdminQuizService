package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public boolean removeQuiz(Quiz quiz) {
        return quizes.removeIf(q -> q.getId().equals(quiz.getId()));
    }

    public Game makeViewable() {
        setQuizes(quizes.stream().map(q -> {
            q.setGame(null);
            return q;
        }).collect(Collectors.toList()));

        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", timeStarted=" + timeStarted +
                ", timeLockedDown=" + timeLockedDown +
                ", timeEnded=" + timeEnded +
                ", quizes=" + quizes +
                '}';
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
