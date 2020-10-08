package com.mirakklian.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mirakklian.dao.UserDao;
import com.mirakklian.entity.User;
import com.mirakklian.exception.UserNotFoundException;

@RestController
public class UserController {

	@Autowired
	UserDao dao;

	@GetMapping(path = "/users")
	public List<User> findAllUsers() {
		return dao.findAll();
	}

	@GetMapping(path = "/users/{id}")
	public EntityModel<User> findUserById(@PathVariable(value = "id") int id) {
		User user = dao.findOne(id);

		if (user == null) {
			throw new UserNotFoundException("User Not Found for ID: " + id);
		}
		EntityModel<User> resource = EntityModel.of(user);

		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAllUsers());

		resource.add(linkTo.withRel("all-users"));

		// HATEOAS

		return resource;
	}

	@DeleteMapping(path = "/users/{id}")
	public void deleteUserById(@PathVariable(value = "id") int id) {
		User user = dao.deleteOneById(id);

		if (user == null) {
			throw new UserNotFoundException("User Not Found for ID: " + id);
		}

	}

	@PostMapping(path = "/add-user")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = dao.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
