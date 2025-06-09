package com.example.patas_board.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class UserForm {

    private Integer id;

    private String account;

    private String password;

    private String name;

    private Integer branchId;

    private Integer departmentId;

    private Integer isStopped;

    private Date createdDate;

    private Date updatedDate;
}
