package com.example.patas_board.controller.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class UserForm {

    private int id;

    @NotNull(message = "アカウントを入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}+$", message = "アカウントは半角英数字かつ6文字以上20文字以下で入力してください")
    private String account;

    @NotNull(message = "パスワードを入力してください")
    private String password;

    private String name;

    private int branchId;

    private int departmentId;

    private int isStopped;

    public enum Status {
        復活, 停止
    }

    private Date createdDate;

    private Date updatedDate;
}
