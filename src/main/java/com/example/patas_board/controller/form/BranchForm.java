package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class BranchForm {

    private Integer id;

    private String name;

    private Date createdDate;

    private Date updatedDate;
}
