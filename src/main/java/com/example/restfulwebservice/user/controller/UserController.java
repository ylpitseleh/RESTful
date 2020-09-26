package com.example.restfulwebservice.user.controller;

import com.example.restfulwebservice.user.UserNotFoundException;
import com.example.restfulwebservice.user.domain.User;
import com.example.restfulwebservice.user.service.UserDaoService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) { // 생성자를 통한 의존성 주입 (@Autowired 써도 되지만 생성자가 더 좋음)
        this.service = service;
    }

    // 전체 사용자 목록 조회
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // 사용자 1명 조회
    // GET /users/1, /users/2 -> 1, 2는 서버측에 문자열(String)으로 전달됨
    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS
        Resource<User>  resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        // 서버로부터 요청 결과값에 적절한 상태 코드를 반환해주는 것이 좋은 API 설계 방식.
        // HTTP Status Code 제어 (GET(200)일 때, POST(201)일 때 성공시 다른 상태 코드)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

    // {id: "1", name: "new name", password: "new password"}
    @PutMapping("/users/{id}")
    public Resource<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User updatedUser = service.update(id, user);

        if (updatedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", user.getId()));
        }

        // HATEOAS
        Resource<User>  resource = new Resource<>(updatedUser);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }
}
