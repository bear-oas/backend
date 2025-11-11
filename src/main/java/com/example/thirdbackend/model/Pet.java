package com.example.thirdbackend.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애완동물 정보")
public class Pet {
    
    @Schema(description = "애완동물 ID", example = "1")
    private Long id;
    
    @Schema(description = "애완동물 이름", example = "멍멍이")
    private String name;
    
    @Schema(description = "종류", example = "강아지", allowableValues = {"강아지", "고양이", "토끼", "햄스터", "새", "기타"})
    private String species;
    
    @Schema(description = "나이 (년)", example = "3")
    private Integer age;
    
    @Schema(description = "상태", example = "available", allowableValues = {"available", "pending", "sold"})
    private String status;

    public Pet() {
    }

    public Pet(Long id, String name, String species, Integer age, String status) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

