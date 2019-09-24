package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.service;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.*;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        if(game.getTimeEnded() != null && game.getTimeEnded().isBefore(LocalDateTime.now()))
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
    private Quiz validate(Integer quizId, Quiz defaultQuiz) {
        if(quizId == null)
            return defaultQuiz;
        Optional<Quiz> oQuiz = repo.quiz().findById(quizId);
        return oQuiz.orElse(defaultQuiz);
    }
    private <T> List<T> validate(List<T> list, List<T> defaultList) {
        if(list == null)
            return defaultList;
        if(list.isEmpty())
            return defaultList;

        return list;
    }
    private LocalDateTime validate(LocalDateTime time, LocalDateTime defaultTime) {
        return (time == null) ? defaultTime : time;
    }

    @Autowired
   private AdminRepo repo;

    //region Create

    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game game){
        String          name = validate(game.getName(), null);
        LocalDateTime   timeLockedDown = game.getTimeLockedDown();

        if(name == null) throw new RuntimeException("Name is required");

        Game newGame = new Game(name, timeLockedDown);
        return repo.game().save(newGame);
    }

    @PostMapping("/createQuiz")
    public Quiz createQuiz(@RequestBody Quiz quiz){
        String      name = validate(quiz.getName(), null);
        Game        game = validate(quiz.getGame(), null);

        if(name == null) throw new RuntimeException("Name is required");
        if(game == null) throw new RuntimeException("Game is required");

        Quiz newQuiz = repo.quiz().save(new Quiz(name, game));
        repo.game().save(game);

        return newQuiz.makeViewable();
    }

    @PostMapping("/createQuestion")
    public Question createQuestion(@RequestBody Question question, int quizId){
        String          slogan = validate(question.getSlogan(), null);
        List<String>    alternatives = question.getAlternatives();
        List<String>    results = question.getResults();
        String          pointsCode = validate(question.getPointsCode(), "");
        String          answerType = validate(question.getAnswerType(), "1");

        Optional<Quiz> oQuiz = repo.quiz().findById(quizId);
        if(!oQuiz.isPresent())
            throw new RuntimeException("Valid quizId is required");
        Quiz quiz = oQuiz.get();

        Question newQuestion = new Question(slogan, alternatives, results, pointsCode, answerType, quiz);
        newQuestion = repo.question().save(newQuestion);

        repo.quiz().save(quiz);

        return newQuestion;
    }

    @PostMapping("/createMatch")
    public Match createMatch(@RequestBody Match match, boolean isTieable, String pointsCode, int quizId){
        String          name = validate(match.getName(), null);
        String          channel = validate(match.getChannel(), "");
        LocalDateTime   date_time = validate(match.getDate_time(), null);
        List<Team>      teams = validate(match.getTeams(), null);

        Team firstTeam = validate(teams.get(0), null);
        Team secondTeam = validate(teams.get(1), null);

        String code = validate(pointsCode, "");
        boolean couldTie = isTieable;
        Optional<Quiz> oQuiz = repo.quiz().findById(quizId);

        if(name == null) throw new RuntimeException("Name is required");
        if(firstTeam == null || secondTeam == null) throw new RuntimeException("Two teams are required");
        if(!oQuiz.isPresent()) throw new RuntimeException("Valid quizId is required");
        Quiz quiz = oQuiz.get();

        Match newMatch = new Match(name, firstTeam, secondTeam, couldTie, date_time, channel, code, quiz);

        repo.question().save(newMatch.getQuestion());
        repo.quiz().save(quiz);
        return repo.match().save(newMatch);
    }

    @PostMapping("/createTeam")
    public Team createTeam(@RequestBody Team team){
        String name = validate(team.getName(), null);
        String flag = validate(team.getFlag(), "");

        if(name == null) throw new RuntimeException("Name is required");

        Team newTeam = new Team(name, flag);
        return repo.team().save(newTeam);
    }

    //endregion

    //region Get by ID

    @GetMapping("/getGame")
    public Optional<Game> getGame(@RequestBody int id) {
        Optional<Game> game = repo.game().findById(id);
        return game.map(Game::makeViewable);
    }

    @GetMapping("/getQuiz")
    public Optional<Quiz> getQuiz(@RequestBody int id) {
        Optional<Quiz> quiz = repo.quiz().findById(id);
        return quiz.map(Quiz::makeViewable);
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
        return StreamSupport.stream(repo.game().findAll().spliterator(), false)
                .map(Game::makeViewable)
                .collect(Collectors.toList());
    }

    @GetMapping("/getQuizes")
    public List<Quiz> getQuizes() {
        return StreamSupport.stream(repo.quiz().findAll().spliterator(), false)
                .map(Quiz::makeViewable)
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
    
    //region Update

    @PutMapping("/updateGame")
    public Game updateGame(@RequestBody Game game) {
        Optional<Game> oGame = repo.game().findById(game.getId());
        if(!oGame.isPresent())
            throw new RuntimeException("No Such Game");
        Game oldGame = oGame.get();


        String          name = validate(game.getName(), oldGame.getName());
        LocalDateTime   timeLockedDown = game.getTimeLockedDown();
        LocalDateTime   timeEnded = game.getTimeEnded();

        timeLockedDown = timeLockedDown == null || timeLockedDown.isBefore(oldGame.getTimeStarted()) ? oldGame.getTimeLockedDown() : timeLockedDown;
        timeEnded = timeEnded == null || timeEnded.isBefore(oldGame.getTimeStarted()) ? oldGame.getTimeEnded() : timeEnded;

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
        Game oldGame = oldQuiz.getGame();

        String      name = validate(quiz.getName(), oldQuiz.getName());
        Game        game = validate(quiz.getGame(), oldGame);

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
        // I don't know why... but they need to be switched...
        Team oldTeam2 = oldMatch.gatherTeam1();
        Team oldTeam1 = oldMatch.gatherTeam2();

        String          name = validate(match.getName(), oldMatch.getName());
        String          channel = validate(match.getChannel(), oldMatch.getChannel());
        LocalDateTime   date_time = match.getDate_time();
        Team            team1 = validate(match.gatherTeam1(), oldTeam1);
        Team            team2 = validate(match.gatherTeam2(), oldTeam2);

        date_time = date_time == null ? oldMatch.getDate_time() : date_time;


        if( !team1.equals(oldTeam1) || !team2.equals(oldTeam2) ) {
            Question question = oldMatch.getQuestion();
            question.setSlogan(team1.getName() + " vs " + team2.getName());
            repo.question().save(question);
        }

        oldMatch.setName(name);
        oldMatch.setChannel(channel);
        oldMatch.setDate_time(date_time);
        oldMatch.setTeam1(team1);
        oldMatch.setTeam2(team2);

        match = repo.match().save(oldMatch);

        return match;
    }



    @PutMapping("/updateQuestion")
    public Question updateQuestion(@RequestBody Question question, Integer quizId) {
        Optional<Question> oQuestion = repo.question().findById(question.getId());
        if(!oQuestion.isPresent())
            throw new RuntimeException("No Such Question");
        Question oldQuestion = oQuestion.get();
        Quiz oldQuiz = repo.quiz().findByQuestionsContains(oldQuestion);

        String          slogan = validate(question.getSlogan(), oldQuestion.getSlogan());
        String          answerType = validate(question.getAnswerType(), oldQuestion.getAnswerType());
        String          pointsCode = validate(question.getPointsCode(), oldQuestion.getPointsCode());
        List<String>    alternatives = validate(question.getAlternatives(), oldQuestion.getAlternatives());
        List<String>    results = validate(question.getResults(), oldQuestion.getResults());
        Quiz            quiz = validate(quizId, oldQuiz);

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

        String      name = validate(team.getName(), oldTeam.getName());
        String      flag = validate(team.getFlag(), oldTeam.getFlag());
        Team        refTeam = validate(team.getRefTeam(), null);

        boolean nameHasChanged = false;
        if(!name.equals(oldTeam.getName())) {
            nameHasChanged = true;
        }

        oldTeam.setName(name);
        oldTeam.setFlag(flag);
        oldTeam.setRefTeam(refTeam);

        if(nameHasChanged)
            repo.match().getMatchesByTeamsContains(oldTeam).forEach(m -> {
                Question q = m.getQuestion();
                q.setSlogan(Match.generateSlogan(m.getTeams()));
                repo.question().save(q);
            });

        team = repo.team().save(oldTeam);

        return team;
    }
    
    //endregion

}
