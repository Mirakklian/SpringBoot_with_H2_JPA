package com.mirakklian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mirakklian.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

}
