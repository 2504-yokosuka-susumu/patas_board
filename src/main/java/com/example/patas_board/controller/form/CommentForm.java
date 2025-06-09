package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class CommentForm {

    private Integer id;

    private String text;

    private Integer userId;

    private Integer messageId;

    private Date createdDate;

    private Date updatedDate;
}
