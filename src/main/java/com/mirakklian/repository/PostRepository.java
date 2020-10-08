package com.mirakklian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mirakklian.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer>{

}
