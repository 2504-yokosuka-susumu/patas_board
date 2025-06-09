package com.example.patas_board.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

    @Column(insertable = false, updatable = false, name="updated_date")
    private Date updatedDate;
}
