package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.service;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.*;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AdminController {

    @Autowired
   private AdminRepo repo;

    //region Create
    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game game){
        String name = game.getName();
        long timeLockedDown = game.getTimeLockedDown();

        Game newGame = new Game(name, timeLockedDown);
        return repo.game().save(newGame);
    }

    @PostMapping("/createQuiz")
    public Quiz createQuiz(@RequestBody Quiz quiz, Integer gameId){
        String name = quiz.getName();
        Game game = repo.game().findById(gameId).get();

        Quiz newQuiz = repo.quiz().save(new Quiz(name, game));
        repo.game().save(game);

        // make viewable
        newQuiz.getGame().setQuizes(newQuiz.getGame().getQuizes().stream()
               // .filter(q -> !q.getId().equals(newQuiz.getId()))
            .map(q -> {
                q = new Quiz(q);
                q.setGame(null);
                return q;
                    }
            ).collect(Collectors.toList()));
        return newQuiz;
    }

    @PostMapping("/createQuestion")
    public Question createQuestion(@RequestBody Question question, int quizId){
        Quiz quiz = repo.quiz().findById(quizId).get();

        String slogan = question.getSlogan();
        List<String> alternatives = question.getAlternatives();
        String pointsCode = question.getPointsCode();
        String answerType = question.getAnswerType();

        Question newQuestion = new Question(slogan, alternatives, pointsCode, answerType, quiz);
        newQuestion = repo.question().save(newQuestion);

        repo.quiz().save(quiz);

        return newQuestion;
    }

    @PostMapping("/createMatch")
    public Match createMatch(@RequestBody Match match, int team1, int team2, boolean isTieable, String pointsCode, int quizId){
        Team firstTeam = repo.team().findById(team1).get();
        Team secondTeam = repo.team().findById(team2).get();
        String channel = match.getChannel();
        Long date_time = match.getDate_time();


        String slogan = firstTeam.getName() + " vs " + secondTeam.getName();
        List<String> alternatives = new ArrayList<>();
        alternatives.add("1");
        if(isTieable) {
            alternatives.add("X");
        }
        alternatives.add("2");
        String code = pointsCode;
        Quiz quiz = repo.quiz().findById(quizId).get();

        Question question = new Question(slogan,alternatives,code,"1", quiz);

        question = repo.question().save(question);
        repo.quiz().save(quiz);

        Match newMatch = new Match(firstTeam, secondTeam, channel, date_time, question);

        return repo.match().save(newMatch);
    }

    @PostMapping("/createTeam")
    public Team createTeam(@RequestBody Team team){
        String name = team.getName();
        String flag = team.getFlag();


        Team newTeam = new Team(name, flag);
        return repo.team().save(newTeam);
    }

    //endregion

    //region Get by ID

    @GetMapping("/getGame")
    public Optional<Game> getGame(@RequestBody int id) {
        Optional<Game> game = repo.game().findById(id);
        if(game.isPresent()) {
            game.get().setQuizes(game.get().getQuizes().stream()
                .map(q -> {
                    q.setGame(null);
                    return q;
                })
                .collect(Collectors.toList()));
        }
        return game;
    }

    @GetMapping("/getQuiz")
    public Optional<Quiz> getQuiz(@RequestBody int id) {
        Optional<Quiz> quiz = repo.quiz().findById(id);
        if(quiz.isPresent()) {
            quiz.get().getGame().setQuizes(
                    quiz.get().getGame().getQuizes().stream()
                        .filter(q -> !q.getId().equals(id))
                        .map(q -> {
                            q.setGame(null);
                            return q;
                        })
                        .collect(Collectors.toList())
            );
        }
        return quiz;
    }

    @GetMapping("/getQuestion")
    public Optional<Question> getQuestion(@RequestBody int id) {
        Optional<Question> question = repo.question().findById(id);
        return question;
    }

    @GetMapping("/getMatch")
    public Optional<Match> getMatch(@RequestBody int id) {
        Optional<Match> match = repo.match().findById(id);
        return match;
    }

    @GetMapping("/getTeam")
    public Optional<Team> getTeam(@RequestBody int id) {
        Optional<Team> team = repo.team().findById(id);
        return team;
    }

    //endregion

    //region Get All

    @GetMapping("/getGames")
    public List<Game> getGames() {
        return StreamSupport.stream(repo.game().findAll().spliterator(), true)
                .map(g -> {
                    g.setQuizes(g.getQuizes().stream()
                            .map(q -> {
                                q.setGame(null);
                                return q;
                            })
                            .collect(Collectors.toList()));
                    return g;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getQuizes")
    public List<Quiz> getQuizes() {
        return StreamSupport.stream(repo.quiz().findAll().spliterator(), true)
                .map(q -> {
                    q.getGame().setQuizes(
                            q.getGame().getQuizes().stream()
                            .filter(q1 -> !q1.getId().equals(q.getId()))
                            .map(q1 -> {
                                q1.setGame(null);
                                return q1;
                            })
                            .collect(Collectors.toList())
                    );
                    return q;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getQuestions")
    public Iterable<Question> getQuestions(){
        return repo.question().findAll();
    }

    @GetMapping("/getMatches")
    public Iterable<Match> getMatches(){
        return repo.match().findAll();
    }

    @GetMapping("/getTeams")
    public Iterable<Team> getTeams(){
        return repo.team().findAll();
    }

    //endregion
    
    //region UPDATE
    
    @PutMapping("/team/{id}/ref")
    public Team addRefTeam(@PathVariable Integer id, @RequestBody Integer refId){
        Optional<Team> team  = repo.team().findById(id);
        Optional<Team> teamRef = repo.team().findById(refId);

        team.get().setRefTeam(teamRef.get());

        return repo.team().save(team.get());

    }
    
    //endregion
}
