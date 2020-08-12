package com.ddng.userapi.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/users/", produces = MediaTypes.HAL_JSON_VALUE)
public class UserController
{
	@Autowired
	UserService userService;

	@Autowired
	ModelMapper modelMapper;

	@PostMapping
	public ResponseEntity createUser(@RequestBody @Valid UserDto dto, Errors errors)
	{
		// Validation 결과 체크
		if (errors.hasErrors())
		{
			return ResponseEntity.badRequest().build();
		}

		// DTO -> Entity 변환
		User mapped = modelMapper.map(dto, User.class);

		// 사용자 저장
		User newUser = userService.save(mapped);

		// uri 생성
		WebMvcLinkBuilder builder = linkTo(UserController.class).slash(newUser.getId());
		URI uri = builder.toUri();

		// HATEOAS 처리
		UserResource resource = new UserResource(newUser);
		resource.add(linkTo(UserController.class).withRel("query-users"));
		resource.add(builder.withRel("update-user"));
		resource.add(builder.withRel("delete-user"));
		resource.add(new Link("/docs/index.html#resources-users-create").withRel("profile"));

		return ResponseEntity.created(uri).body(resource);
	}
}
