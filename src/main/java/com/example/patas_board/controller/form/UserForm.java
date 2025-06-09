package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class UserForm {

    private int id;

    private String account;

    private String password;

    private String name;

    private int branchId;

    private int departmentId;

    private int isStopped;

    private Date createdDate;

    private Date updatedDate;
}
