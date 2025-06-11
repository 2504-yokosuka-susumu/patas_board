package com.example.patas_board.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    @Id
    private int id;

    private String title;

    private String text;

    private int userId;

    private String category;

    private String name;

    private String account;

    private Date createdDate;

    private Date updatedDate;
}
