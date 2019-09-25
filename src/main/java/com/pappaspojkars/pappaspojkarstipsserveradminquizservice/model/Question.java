package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String slogan;
    @ElementCollection(targetClass=String.class)
    private List<String> alternatives;
    @ElementCollection(targetClass=String.class)
    private List<String> results;
    private String pointsCode;
    private String answerType;

    public Question() {
    }

    public Question(String slogan, List<String> alternatives, List<String> results, String pointsCode, String answerType, Quiz quiz) {
        this.slogan = slogan;
        this.alternatives = alternatives;
        this.results = results;
        this.pointsCode = pointsCode;
        this.answerType = answerType;
        quiz.addQuestion(this);
    }
    public Question(String slogan, List<String> alternatives,  String pointsCode, String answerType, Quiz quiz) {
        this.slogan = slogan;
        this.alternatives = alternatives;
        this.pointsCode = pointsCode;
        this.answerType = answerType;
        this.results = new ArrayList<>();
        quiz.addQuestion(this);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", slogan='" + slogan + '\'' +
                ", alternatives=" + alternatives +
                ", results=" + results +
                ", pointsCode='" + pointsCode + '\'' +
                ", answerType='" + answerType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
