package com.demo.consumerapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.demo.consumerapp.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
