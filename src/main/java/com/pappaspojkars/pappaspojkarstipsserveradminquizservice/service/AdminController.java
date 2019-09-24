package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.service;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.*;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AdminController {

    private String validate(String string, String defaultString) {
        return string == null || string.trim().isEmpty() ? defaultString : string;
    }
    private Game validate(Game game, Game defaultGame) {
        if(game == null)
            return defaultGame;
        Optional<Game> oGame = repo.game().findById(game.getId());
        if(!oGame.isPresent())
            return defaultGame;
        game = oGame.get();
        if(game.getTimeEnded().isBefore(LocalDateTime.now()))
            return defaultGame;

        return game;
    }
    private Team validate(Team team, Team defaultTeam) {
        if(team == null)
            return defaultTeam;
        Optional<Team> oTeam = repo.team().findById(team.getId());
        if(!oTeam.isPresent())
            return defaultTeam;
        team = oTeam.get();

        return team;
    }
    private Quiz validate(int quizId, Quiz defaultQuiz) {
        Optional<Quiz> oQuiz = repo.quiz().findById(quizId);
        return oQuiz.orElse(defaultQuiz);
    }
    private List<String> validate(List<String> list, List<String> defaultList) {
        if(list == null)
            return defaultList;
        if(list.isEmpty())
            return defaultList;

        return list;
    }

    @Autowired
   private AdminRepo repo;

    //region Create
    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game game){
        String name = game.getName();
        LocalDateTime timeLockedDown = game.getTimeLockedDown();

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
        List<String> results = question.getResults();
        String pointsCode = question.getPointsCode();
        String answerType = question.getAnswerType();

        Question newQuestion = new Question(slogan, alternatives, results, pointsCode, answerType, quiz);
        newQuestion = repo.question().save(newQuestion);

        repo.quiz().save(quiz);

        return newQuestion;
    }

    @PostMapping("/createMatch")
    public Match createMatch(@RequestBody Match match, int team1, int team2, boolean isTieable, String pointsCode, int quizId){
        String name = match.getName();
        Team firstTeam = repo.team().findById(team1).get();
        Team secondTeam = repo.team().findById(team2).get();
        String channel = match.getChannel();
        LocalDateTime date_time = match.getDate_time();

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

        Match newMatch = new Match(name, firstTeam, secondTeam, channel, date_time, question);

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

    @PutMapping("/updateGame")
    public Game updateGame(@RequestBody Game game) {
        Optional<Game> oGame = repo.game().findById(game.getId());
        if(!oGame.isPresent())
            throw new RuntimeException("No Such Game");
        Game oldGame = oGame.get();


        String name = validate(game.getName(), oldGame.getName());

        LocalDateTime timeLockedDown = game.getTimeLockedDown();
        timeLockedDown = timeLockedDown == null || timeLockedDown.isBefore(game.getTimeStarted()) ? oldGame.getTimeLockedDown() : timeLockedDown;

        LocalDateTime timeEnded = game.getTimeEnded();
        timeEnded = timeEnded == null || timeEnded.isBefore(game.getTimeStarted()) ? oldGame.getTimeEnded() : timeEnded;

        oldGame.setName(name);
        oldGame.setTimeLockedDown(timeLockedDown);
        oldGame.setTimeEnded(timeEnded);

        game = repo.game().save(oldGame);

        return game.makeViewable();
    }


    @PutMapping("/updateQuiz")
    public Quiz updateQuiz(@RequestBody Quiz quiz) {
        Optional<Quiz> oQuiz = repo.quiz().findById(quiz.getId());
        if(!oQuiz.isPresent())
            throw new RuntimeException("No Such Quiz");
        Quiz oldQuiz = oQuiz.get();

        String name = validate(quiz.getName(), oldQuiz.getName());

        Game oldGame = oldQuiz.getGame();
        Game game = validate(quiz.getGame(), oldGame);
        if(!game.getId().equals(oldGame.getId())) {
            oldGame.removeQuiz(quiz);
            game.addQuiz(quiz);

            repo.game().save(oldGame);
            repo.game().save(game);
        }

        oldQuiz.setName(name);
        oldQuiz.setGame(game);

        quiz = repo.quiz().save(oldQuiz);
        return quiz.makeViewable();
    }


    @PutMapping("/updateMatch")
    public Match updateMatch(@RequestBody Match match) {
        Optional<Match> oMatch = repo.match().findById(match.getId());
        if(!oMatch.isPresent())
            throw new RuntimeException("No Such Match");
        Match oldMatch = oMatch.get();

        Team oldTeam1 = oldMatch.getTeams().get(0);
        Team oldTeam2 = oldMatch.getTeams().get(1);

        String name = validate(match.getName(), oldMatch.getName());
        String channel = validate(match.getChannel(), oldMatch.getName());
        LocalDateTime date_time = match.getDate_time();
        List<Team> teams = match.getTeams();

        date_time = date_time == null ? oldMatch.getDate_time() : date_time;
        Team team1 = validate(teams.get(0), oldTeam1);
        Team team2 = validate(teams.get(1), oldTeam2);


        if( team1.getId().equals(oldTeam1.getId()) || !team2.getId().equals(oldTeam2.getId()) ) {
            Question question = oldMatch.getQuestion();
            question.setSlogan(team1.getName() + " vs " + team2.getName());
            repo.question().save(question);
        }

        oldMatch.setName(name);
        oldMatch.setChannel(channel);
        oldMatch.setDate_time(date_time);
        oldMatch.setTeams(Arrays.asList(team1, team2));

        match = repo.match().save(oldMatch);

        return match;
    }



    @PutMapping("/updateQuestion")
    public Question updateQuestion(@RequestBody Question question, int quizId) {
        Optional<Question> oQuestion = repo.question().findById(question.getId());
        if(!oQuestion.isPresent())
            throw new RuntimeException("No Such Question");
        Question oldQuestion = oQuestion.get();
        Quiz oldQuiz = repo.quiz().findByQuestionsContains(oldQuestion);

        String slogan = validate(question.getSlogan(), oldQuestion.getSlogan());
        String answerType = validate(question.getAnswerType(), oldQuestion.getAnswerType());
        String pointsCode = validate(question.getPointsCode(), oldQuestion.getPointsCode());
        List<String> alternatives = validate(question.getAlternatives(), oldQuestion.getAlternatives());
        List<String> results = validate(question.getResults(), oldQuestion.getResults());
        Quiz quiz = validate(quizId, oldQuiz);

        if(!quiz.getId().equals(oldQuiz.getId())) {
            oldQuiz.removeQuestion(question);
            quiz.addQuestion(question);

            repo.quiz().save(oldQuiz);
            repo.quiz().save(quiz);
        }

        oldQuestion.setSlogan(slogan);
        oldQuestion.setAnswerType(answerType);
        oldQuestion.setPointsCode(pointsCode);
        oldQuestion.setAlternatives(alternatives);
        oldQuestion.setResults(results);

        question = repo.question().save(oldQuestion);

        return question;
    }

    @PutMapping("/updateTeam")
    public Team updateTeam(@RequestBody Team team) {
        Optional<Team> oTeam = repo.team().findById(team.getId());
        if(!oTeam.isPresent())
            throw new RuntimeException("No Such Team");
        Team oldTeam = oTeam.get();

        String name = validate(team.getName(), oldTeam.getName());
        String flag = validate(team.getFlag(), oldTeam.getFlag());
        Team refTeam = team.getRefTeam();

        if(!name.equals(oldTeam.getName())) {
            repo.match().getMatchesByTeamsContains(oldTeam)
                    .forEach(m -> {
                        Question q = m.getQuestion();
                        List<Team> teams = m.getTeams();
                        int teamIndex = teams.indexOf(oldTeam);

                        String team1name = teamIndex == 0 ? name : teams.get(0).getName();
                        String team2name = teamIndex == 1 ? name : teams.get(1).getName();

                        q.setSlogan(team1name + " vs " + team2name);
                        repo.question().save(q);
                    });
        }

        oldTeam.setName(name);
        oldTeam.setFlag(flag);
        oldTeam.setRefTeam(refTeam);

        team = repo.team().save(oldTeam);

        return team;
    }
    
    //endregion

    @GetMapping("/test")
    public LocalDateTime test() {
        return LocalDateTime.now();
    }
}
