package com.klaeck.techtask.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    public String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}
