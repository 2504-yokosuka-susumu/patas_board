package com.example.patas_board.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private String category;

    @Column(name = "user_id")
    private Integer userId;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

    @Column(insertable = false, updatable = false, name="updated_date")
    private Date updatedDate;
}
