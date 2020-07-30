package com.ddng.userapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController
{
	@Autowired
	UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity findUser(@PathVariable Long id)
	{
		Optional<User> findById = userService.findById(id);

		if(findById.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}

		User user = findById.get();

		return ResponseEntity.ok(user);
	}
}
