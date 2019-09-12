package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends CrudRepository<Game,Integer> {
}
