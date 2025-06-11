package com.example.patas_board.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Table(name ="comments")
@SecondaryTable(name = "users",
        pkJoinColumns = {@PrimaryKeyJoinColumn(name = "name"),
        @PrimaryKeyJoinColumn(name = "account")})
public class UserComment {
    @Column(name = "id")
    private int id;

    @Column(name = "text")
    private int text;

    @Column(name = "message_id")
    private int messageId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private int name;

    @Column(name = "account")
    private int account;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

    @Column(insertable = false, updatable = false, name="updated_date")
    private Date updatedDate;
}
