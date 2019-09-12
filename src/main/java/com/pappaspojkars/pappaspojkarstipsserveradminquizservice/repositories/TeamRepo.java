package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends CrudRepository<Team,Integer> {
}
