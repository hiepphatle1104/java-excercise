package backend.project.com.backendproject.repository;


import backend.project.com.backendproject.model.RedisToken;
import backend.project.com.backendproject.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
    User findByUserID(String id);
}
