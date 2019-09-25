package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Match;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends CrudRepository<Match,Integer> {
    List<Match> getMatchesByTeamsContains(Team team);
}
