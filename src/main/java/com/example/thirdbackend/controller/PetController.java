package com.example.thirdbackend.controller;

import com.example.thirdbackend.model.Pet;
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
@RequestMapping("/api/pets")
@Tag(name = "Pet", description = "애완동물 관리 API")
public class PetController {

    private final List<Pet> pets = new ArrayList<>();
    private Long nextId = 1L;

    public PetController() {
        // 초기 데이터 추가
        pets.add(new Pet(nextId++, "멍멍이", "강아지", 3, "available"));
        pets.add(new Pet(nextId++, "야옹이", "고양이", 2, "available"));
        pets.add(new Pet(nextId++, "토리", "토끼", 1, "pending"));
        pets.add(new Pet(nextId++, "치즈", "햄스터", 1, "sold"));
    }

    @Operation(summary = "모든 애완동물 조회", description = "시스템에 등록된 모든 애완동물 목록을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Pet.class)))
    })
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets(
            @Parameter(description = "상태 필터 (available, pending, sold)", required = false)
            @RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty()) {
            List<Pet> filteredPets = pets.stream()
                    .filter(p -> p.getStatus().equalsIgnoreCase(status))
                    .toList();
            return ResponseEntity.ok(filteredPets);
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "애완동물 ID로 조회", description = "특정 ID를 가진 애완동물 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "애완동물을 찾음",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "404", description = "애완동물을 찾을 수 없음",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(
            @Parameter(description = "애완동물 ID", required = true, example = "1")
            @PathVariable Long id) {
        Optional<Pet> pet = pets.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        
        return pet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "새 애완동물 등록", description = "새로운 애완동물을 시스템에 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "애완동물이 성공적으로 등록됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Pet> createPet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 애완동물 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Pet.class)))
            @RequestBody Pet pet) {
        pet.setId(nextId++);
        pets.add(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }

    @Operation(summary = "애완동물 정보 수정", description = "기존 애완동물의 정보를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "애완동물 정보가 성공적으로 수정됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "404", description = "애완동물을 찾을 수 없음",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @Parameter(description = "애완동물 ID", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 애완동물 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Pet.class)))
            @RequestBody Pet updatedPet) {
        Optional<Pet> petOptional = pets.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            pet.setName(updatedPet.getName());
            pet.setSpecies(updatedPet.getSpecies());
            pet.setAge(updatedPet.getAge());
            pet.setStatus(updatedPet.getStatus());
            return ResponseEntity.ok(pet);
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "애완동물 상태 업데이트", description = "애완동물의 상태만 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상태가 성공적으로 업데이트됨",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "404", description = "애완동물을 찾을 수 없음",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 상태 값",
                content = @Content)
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pet> updatePetStatus(
            @Parameter(description = "애완동물 ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "새로운 상태 (available, pending, sold)", required = true)
            @RequestParam String status) {
        
        // 상태 값 검증
        if (!status.equals("available") && !status.equals("pending") && !status.equals("sold")) {
            return ResponseEntity.badRequest().build();
        }
        
        Optional<Pet> petOptional = pets.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            pet.setStatus(status);
            return ResponseEntity.ok(pet);
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "애완동물 삭제", description = "특정 ID를 가진 애완동물을 시스템에서 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "애완동물이 성공적으로 삭제됨"),
        @ApiResponse(responseCode = "404", description = "애완동물을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "애완동물 ID", required = true, example = "1")
            @PathVariable Long id) {
        boolean removed = pets.removeIf(p -> p.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}

