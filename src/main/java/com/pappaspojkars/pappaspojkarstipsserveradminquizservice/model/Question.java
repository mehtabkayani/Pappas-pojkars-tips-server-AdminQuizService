package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String slogan;
    @ElementCollection(targetClass=String.class)
    private List<String> alternatives;
    @ElementCollection(targetClass=String.class)
    private List<String> results;
    private String pointsCode;
    private String answerType;

    public Question(String slogan, List<String> alternatives, List<String> results, String pointsCode, String answerType) {
        this.slogan = slogan;
        this.alternatives = alternatives;
        this.results = results;
        this.pointsCode = pointsCode;
        this.answerType = answerType;
    }
    public Question(String slogan, List<String> alternatives,  String pointsCode, String answerType, Quiz quiz) {
        this.slogan = slogan;
        this.alternatives = alternatives;
        this.pointsCode = pointsCode;
        this.answerType = answerType;
        this.results = new ArrayList<>();
        quiz.addQuestion(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getPointsCode() {
        return pointsCode;
    }

    public void setPointsCode(String pointsCode) {
        this.pointsCode = pointsCode;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }
}
