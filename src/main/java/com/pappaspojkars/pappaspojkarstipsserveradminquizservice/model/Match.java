package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.utilities.LocalDateTimeAttributeConverter;
import javafx.util.Pair;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "duel")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String channel;
    private String name;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date_time;
    @ManyToMany
    private List<Team> teams;
    @OneToOne
    private Question question;

    public static String generateSlogan(List<Team> teams) {
        if(teams == null)
            return null;

        return teams.stream()
                .map(Team::getName)
                .reduce((t1,t2) -> t1 + " vs " + t2)
                .orElse(null);
    }

    public Match() {
    }

    public Match(String name, Team team1, Team team2, String channel, LocalDateTime date_time, Question question) {
        this.teams = Arrays.asList(team1, team2);

        this.name = name;
        this.channel = channel;
        this.date_time = date_time;
        this.question = question;
    }

    public Match(String name, Team team1, Team team2, boolean isTieable, LocalDateTime date_time, String channel, String pointsCode, Quiz quiz) {
        this.teams = Arrays.asList(team1, team2);
        this.name = name;
        this.date_time = date_time;
        this.channel = channel;


        String slogan = Match.generateSlogan(this.teams);
        List<String> alternatives = new ArrayList<>();
        alternatives.add("1");
        if(isTieable) {
            alternatives.add("X");
        }
        alternatives.add("2");

        this.question = new Question(slogan,alternatives,pointsCode,"1", quiz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id.equals(match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", channel='" + channel + '\'' +
                ", name='" + name + '\'' +
                ", date_time=" + date_time +
                ", teams=" + teams +
                ", question=" + question +
                '}';
    }

    public Team gatherTeam1() {
        if(teams == null)
            return null;
        return teams.get(0);
    }

    public void setTeam1(Team team) {
        if(teams == null)
            return;
        teams.set(0, team);
        question.setSlogan(Match.generateSlogan(this.teams));
    }

    public Team gatherTeam2() {
        if(teams == null)
            return null;
        return teams.get(1);
    }

    public void setTeam2(Team team) {
        if(teams == null)
            return;
        teams.set(1, team);
        question.setSlogan(Match.generateSlogan(this.teams));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
