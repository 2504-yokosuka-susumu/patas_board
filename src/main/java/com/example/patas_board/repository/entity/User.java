package com.example.patas_board.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String account;

    @Column
    private String password;

    @Column
    private String name;

    @Column(name = "branch_id")
    private int branchId;

    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "is_stopped")
    private int isStopped;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

    @Column(insertable = false, updatable = false, name="updated_date")
    private Date updatedDate;

    @Column(insertable = false, updatable = false, name="login_date")
    private Date loginDate;
}
