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
    private String portolioUrl;

    @OneToMany(mappedBy="student")
    private List<Project> projects;

    public Student() {
    }

    public Student(String fanPageUrl, String portolioUrl, List<Project> projects) {
        this.fanPageUrl = fanPageUrl;
        this.portolioUrl = portolioUrl;
        this.projects = projects;
    }

    public String getFanPageUrl() {
        return fanPageUrl;
    }

    public void setFanPageUrl(String fanPageUrl) {
        this.fanPageUrl = fanPageUrl;
    }

    public String getPortolioUrl() {
        return portolioUrl;
    }

    public void setPortolioUrl(String portolioUrl) {
        this.portolioUrl = portolioUrl;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
