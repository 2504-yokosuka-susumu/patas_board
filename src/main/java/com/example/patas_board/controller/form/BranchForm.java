package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class BranchForm {

    private int id;

    private String name;

    private int totalPost;

    private int totalComment;

    private Date createdDate;

    private Date updatedDate;
}
