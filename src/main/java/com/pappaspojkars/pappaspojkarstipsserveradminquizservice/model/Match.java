package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToMany
    private List<Team> teams;
    private String channel;
    private Long date_time;
    @OneToOne
    private Question question;

    public Match(Team team1, Team team2, boolean isTieable, Long date_time, String channel, String pointsCode) {
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

        this.question = new Question(slogan,alternatives,pointsCode,"1");
        this.date_time = date_time;
        this.channel = channel;
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

    public Long getDate_time() {
        return date_time;
    }

    public void setDate_time(Long date_time) {
        this.date_time = date_time;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
