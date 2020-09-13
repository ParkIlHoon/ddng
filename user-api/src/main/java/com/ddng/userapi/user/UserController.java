package com.ddng.userapi.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>사용자 관련 요청 처리 컨트롤러</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class UserController
{
	private final UserService userService;
	private final ModelMapper modelMapper;

	/**
	 * 사용자를 생성한다.
	 * @param dto 생성할 사용자 정보
	 * @param errors Validation 결과
	 */
	@PostMapping
	public ResponseEntity createUser(@RequestBody @Valid UserDto.Create dto, Errors errors)
	{
		// Validation 결과 체크
		if (errors.hasErrors())
		{
			return ResponseEntity.badRequest().build();
		}

		// 사용자 신규 생성
		User newUser = userService.createUser(dto);

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

	/**
	 * 사용자 목록을 검색한다.
	 * @param dto 검색할 정보
	 */
	@GetMapping
	public ResponseEntity queryUser(@ModelAttribute UserDto.Read dto)
	{
		// 사용자 조회
		List<UserDto.Read> users = userService.searchEq(dto);

		// URI 생성
		WebMvcLinkBuilder builder = linkTo(UserController.class);
		URI uri = builder.toUri();

		//TODO 사용자 정보별 HATEOAS 정보 삽입

		return ResponseEntity.ok(users);
	}

	/**
	 * 사용자 한 명을 조회한다.
	 * @param id 조회할 사용자의 아이디
	 */
	@GetMapping("/{id}")
	public ResponseEntity getUser(@PathVariable Long id)
	{
		// 사용자 조회
		Optional<User> optional = userService.findById(id);

		if (optional.isEmpty())
		{
			return ResponseEntity.badRequest().build();
		}

		// uri 생성
		WebMvcLinkBuilder builder = linkTo(UserController.class).slash(optional.get().getId());
		URI uri = builder.toUri();

		// HATEOAS 처리
		UserResource resource = new UserResource(optional.get());
		resource.add(linkTo(UserController.class).withRel("query-users"));
		resource.add(builder.withRel("update-user"));
		resource.add(builder.withRel("delete-user"));
		resource.add(new Link("/docs/index.html#resources-get-user").withRel("profile"));

		return ResponseEntity.ok(resource);
	}

	/**
	 * 사용자 정보를 수정한다.
	 * @param id 수정할 사용자의 아이디
 	 * @param dto 수정할 정보 DTO
	 * @param errors
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity updateUser(@PathVariable Long id,
									 @Valid @RequestBody UserDto.Update dto,
									 Errors errors)
	{
		// Validation 결과 체크
		if (errors.hasErrors())
		{
			return ResponseEntity.badRequest().build();
		}

		// 사용자 조회
		Optional<User> byId = userService.findById(id);
		if(byId.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}
		else
		{
			// 사용자 정보 수정
			User save = userService.updateUser(byId.get(), dto);

			// HATEOAS 처리
			WebMvcLinkBuilder builder = linkTo(UserController.class).slash(save.getId());
			UserResource resource = new UserResource(save);
			resource.add(linkTo(UserController.class).withRel("query-users"));
			resource.add(builder.withRel("update-user"));
			resource.add(builder.withRel("delete-user"));
			resource.add(new Link("/docs/index.html#resources-update-user").withRel("profile"));

			return ResponseEntity.ok(resource);
		}
	}

	/**
	 * 사용자를 제거한다.
	 * @param id 제거할 사용자 아이디
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable Long id)
	{
		// 사용자 조회
		Optional<User> byId = userService.findById(id);
		if(byId.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}

		// 사용자 삭제
		userService.deleteUser(byId.get());

		return ResponseEntity.ok().build();
	}
}
