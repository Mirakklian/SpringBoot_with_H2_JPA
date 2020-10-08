package com.mirakklian.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mirakklian.entity.User;

@Component
public class UserDao {

	private static int userCount = 3;
	private static List<User> users = new ArrayList<>();

	static {

		users.add(new User(1, "Adam", new Date()));
		users.add(new User(2, "Eva", new Date()));
		users.add(new User(3, "Kaka", new Date()));

	}

	public List<User> findAll() {

		return users;
	}

	public User save(User user) {
		if (user.getId() == null) {
			user.setId(++userCount);
		}
		users.add(user);
		return user;
	}

	public User findOne(int id) {

		// User user=users.stream().filter(o->o.getId()==id).findAny().get();

		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}

		return null;
	}

	public User deleteOneById(int id) {
		Iterator<User> iterator=users.iterator();
		while(iterator.hasNext()) {
			User user=iterator.next();
			if (user.getId() == id) {
				iterator.remove();
				return user;
			}
		}

		return null;
	}

}
