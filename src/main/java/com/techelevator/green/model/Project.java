package com.techelevator.green.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.techelevator.green.model.view.View;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonView(View.NonAdmin.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @JsonView(View.NonAdmin.class)
    private String url;

    @JsonView(View.NonAdmin.class)
    private String name;

    @JsonView(View.NonAdmin.class)
    private String description;
    
}
