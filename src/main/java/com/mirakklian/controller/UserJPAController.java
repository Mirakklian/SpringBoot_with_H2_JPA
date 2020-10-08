package com.mirakklian.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.mirakklian.entity.Post;
import com.mirakklian.entity.User;
import com.mirakklian.exception.UserNotFoundException;
import com.mirakklian.repository.PostRepository;
import com.mirakklian.repository.UserRepository;

@RestController
public class UserJPAController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PostRepository postRepository;

	@GetMapping(path = "jpa/users")
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping(path = "jpa/users/{id}")
	public EntityModel<User> findUserById(@PathVariable(value = "id") int id) {
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent()) {
			throw new UserNotFoundException("User Not Found for ID: " + id);
		}
		EntityModel<User> resource = EntityModel.of(user.get());

		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAllUsers());

		resource.add(linkTo.withRel("all-users"));

		// HATEOAS

		return resource;
	}

	@DeleteMapping(path = "jpa/users/{id}")
	public void deleteUserById(@PathVariable(value = "id") int id) {
		userRepository.deleteById(id);

	}

	@PostMapping(path = "jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	//Post implementation
	
	@GetMapping(path = "jpa/users/{id}/posts")
	public List<Post> findAllUsers(@PathVariable int id) {
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("User Not Found for ID: " + id);
		}
		return userOptional.get().getPost();
	}
	
	@PostMapping(path = "jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id,@RequestBody Post post) {
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("User Not Found for ID: " + id);
		}
		
		User user=userOptional.get();
		post.setUser(user);
		postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
