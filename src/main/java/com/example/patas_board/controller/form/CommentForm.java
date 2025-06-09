package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class CommentForm {

    private int id;

    private String text;

    private int userId;

    private int messageId;

    private Date createdDate;

    private Date updatedDate;
}
