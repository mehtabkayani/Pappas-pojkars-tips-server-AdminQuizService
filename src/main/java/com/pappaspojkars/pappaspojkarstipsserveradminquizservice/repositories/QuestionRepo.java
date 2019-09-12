package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.repositories;

import com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Integer> {
}
