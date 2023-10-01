package com.techelevator.green.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    private Long id;

    @Column(name = "fan_page_url")
    private String fanPageUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @OneToMany(mappedBy="student")
    private List<Project> projects;

    public Student() {
    }

    public Student(String fanPageUrl, String portfolioUrl, List<Project> projects) {
        this.fanPageUrl = fanPageUrl;
        this.portfolioUrl = portfolioUrl;
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFanPageUrl() {
        return fanPageUrl;
    }

    public void setFanPageUrl(String fanPageUrl) {
        this.fanPageUrl = fanPageUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
