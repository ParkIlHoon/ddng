package com.ddng.userapi.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{
	@GetMapping("/")
	public String main()
	{
		return "test User Information";
	}

}
