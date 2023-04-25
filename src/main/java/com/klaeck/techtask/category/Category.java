package com.klaeck.techtask.category;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}
