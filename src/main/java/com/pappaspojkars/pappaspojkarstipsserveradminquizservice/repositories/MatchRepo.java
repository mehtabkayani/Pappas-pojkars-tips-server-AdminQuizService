package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepo extends CrudRepository<Match,Integer> {
}
