package com.example.thirdbackend.controller;

import com.example.thirdbackend.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private Long nextId = 1L;

    public UserController() {
        // 초기 데이터 추가
        users.add(new User(nextId++, "홍길동", "hong@example.com", 25));
        users.add(new User(nextId++, "김영희", "kim@example.com", 30));
        users.add(new User(nextId++, "이철수", "lee@example.com", 28));
    }

    @Operation(summary = "모든 사용자 조회", description = "시스템에 등록된 모든 사용자 목록을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class)))
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "사용자 ID로 조회", description = "특정 ID를 가진 사용자 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자를 찾음",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "새 사용자 생성", description = "새로운 사용자를 시스템에 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자가 성공적으로 생성됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 사용자 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @RequestBody User user) {
        user.setId(nextId++);
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 수정됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 사용자 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @RequestBody User updatedUser) {
        Optional<User> userOptional = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setAge(updatedUser.getAge());
            return ResponseEntity.ok(user);
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "사용자 삭제", description = "특정 ID를 가진 사용자를 시스템에서 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "사용자가 성공적으로 삭제됨"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}

