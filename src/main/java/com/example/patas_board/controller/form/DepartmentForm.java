package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentForm {

    private int id;

    private String name;

    private Date createdDate;

    private Date updatedDate;
}
