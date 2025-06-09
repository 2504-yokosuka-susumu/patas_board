package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class MessageForm {

    private int id;

    private String title;

    private String text;

    private String category;

    private int userId;

    private Date createdDate;

    private Date updatedDate;
}
