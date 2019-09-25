package com.pappaspojkars.pappaspojkarstipsserveradminquizservice.model;

import javax.persistence.*;
import java.util.Objects;


@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String flag;
    @OneToOne
    private Team refTeam;


    public Team(String name, String flag) {
        this.name = name;
        this.flag = flag;
        this.refTeam = null;
    }

    public Team() {
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                ", refTeam=" + refTeam +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Team getRefTeam() {
        return refTeam;
    }

    public void setRefTeam(Team refTeam) {
        this.refTeam = refTeam;
    }
}
