package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany
    private List<Question> questions;

    @ManyToOne
    private Game game;

    public void addQuestion(Question question) {
        questions.add(question);
    }
    public boolean removeQuestion(Question question) {
        return questions.removeIf(q -> q.getId().equals(question.getId()));
    }
    public Quiz makeViewable() {
        game.setQuizes(game.getQuizes().stream()
            // .filter(q -> !q.getId().equals(newQuiz.getId()))
            .map(q -> {
                        q = new Quiz(q);
                        q.setGame(null);
                        return q;
                    }
            ).collect(Collectors.toList()));
        return this;
    }

    public Quiz(){}

    public Quiz(Quiz quiz) {
        id = quiz.id;
        name = quiz.getName();
        questions = quiz.getQuestions();
        game = quiz.getGame();
    }

    public Quiz(List<Question> questions, String name, Game game) {
        this.name = name;
        this.questions = questions;
        this.game = game;
    }

    public Quiz(String name, Game game) {
        this.name = name;
        this.questions = new ArrayList<>();
        this.game = game;
        game.addQuiz(this);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", questions=" + questions +
                ", game=" + game +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }



}