package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.Utilities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    @OneToMany
    private List<Quiz> quizes;
    private Long timeStarted;
    private Long timeLockedDown;
    private Long timeEnded;

    public Game(String name, List<Quiz> quizes, Long timeLockedDown) {
        this.name = name;
        this.quizes = quizes;
        this.timeLockedDown = timeLockedDown;

        this.timeStarted = LocalDateTime.now().toEpochSecond(Utilities.SERVER_OFFSET);
    }

    public Game(String name, Long timeLockedDown) {
        this.name = name;
        this.quizes = new ArrayList<>();
        this.timeLockedDown = timeLockedDown;

        this.timeStarted = LocalDateTime.now().toEpochSecond(Utilities.SERVER_OFFSET);
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

    public List<Quiz> getQuizes() {
        return quizes;
    }

    public void setQuizes(List<Quiz> quizes) {
        this.quizes = quizes;
    }

    public Long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Long timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Long getTimeLockedDown() {
        return timeLockedDown;
    }

    public void setTimeLockedDown(Long timeLockedDown) {
        this.timeLockedDown = timeLockedDown;
    }

    public Long getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Long timeEnded) {
        this.timeEnded = timeEnded;
    }
}
