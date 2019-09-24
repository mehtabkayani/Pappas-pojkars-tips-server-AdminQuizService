package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities.LocalDateTimeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter

@Entity
@Table(name = "duel")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String channel;
    private String name;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date_time;
    @ManyToMany
    private List<Team> teams;
    @OneToOne
    private Question question;

    public Match(String name, Team team1, Team team2, String channel, LocalDateTime date_time, Question question) {
        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        this.name = name;
        this.channel = channel;
        this.date_time = date_time;
        this.question = question;
    }

    public Match(String name, Team team1, Team team2, boolean isTieable, LocalDateTime date_time, String channel, String pointsCode, Quiz quiz) {
        String slogan =team1.getName() + " vs " + team2.getName();
        List<String> alternatives = new ArrayList<>();
        alternatives.add("1");
        if(isTieable) {
            alternatives.add("X");
        }
        alternatives.add("2");
        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        this.name = name;
        this.question = new Question(slogan,alternatives,pointsCode,"1", quiz);
        this.date_time = date_time;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LocalDateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
