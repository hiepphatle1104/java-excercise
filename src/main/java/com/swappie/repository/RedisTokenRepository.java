package com.swappie.repository;


import com.swappie.model.RedisToken;
import com.swappie.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
    User findByUserID(String id);
}
