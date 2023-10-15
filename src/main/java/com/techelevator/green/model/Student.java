package com.techelevator.green.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.techelevator.green.model.auth.User;
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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "fan_page_url")
    private String fanPageUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy="student")
    private List<Project> projects;

}
