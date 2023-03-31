package com.kosnik.repository;

import com.kosnik.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, DynamicQueryUserRepository{
    Optional<User> findByEmail(String email);
}