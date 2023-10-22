package com.techelevator.green.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "student_id"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    @JsonIncludeProperties({"id", "user"})
    private Student student;

    private String url;

    private String name;

    private String description;
    
}
