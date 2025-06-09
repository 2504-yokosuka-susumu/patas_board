package com.example.patas_board.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String text;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

    @Column(insertable = false, updatable = false, name="updated_date")
    private Date updatedDate;
}
