package com.example.patas_board.controller.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class UserCommentForm {

    @Id
    private int id;

    private String text;

    private int userId;

    private  String name;

    private  String account;

    private Date createdDate;

    private Date updatedDate;
}
