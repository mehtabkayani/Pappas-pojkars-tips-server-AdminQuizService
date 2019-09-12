package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepo extends CrudRepository<Quiz, Integer> {
}
