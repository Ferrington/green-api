package com.techelevator.green.model;

import com.fasterxml.jackson.annotation.*;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.model.view.View;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "fanPageUrl", "portfolioUrl", "projects"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.NonAdmin.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    @JsonView(View.NonAdmin.class)
    private User user;

    @Column(name = "fan_page_url")
    @JsonView(View.NonAdmin.class)
    private String fanPageUrl;

    @Column(name = "portfolio_url")
    @JsonView(View.NonAdmin.class)
    private String portfolioUrl;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy="student")
    @JsonView(View.NonAdmin.class)
    private List<Project> projects;

}
