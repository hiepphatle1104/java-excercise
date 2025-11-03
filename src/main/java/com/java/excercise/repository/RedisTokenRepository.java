package com.java.excercise.repository;


import com.java.excercise.model.entities.RedisToken;
import com.java.excercise.model.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
    User findByUserID(String id);
}
