package com.example.patas_board.controller.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class UserForm {

    private int id;

    @NotBlank(message = "アカウントを入力してください")
    //トンガリ＄で空白の許可、|で(or)
    @Pattern(regexp = "^$|^[a-zA-Z0-9]{6,20}+$", message = "アカウントは半角英数字かつ6文字以上20文字以下で入力してください")
    private String account;

    private String password;

    @NotBlank(message = "氏名を入力してください")
    @Size(max = 10, message = "氏名は10文字以下で入力してください")
    private String name;

    @NotNull(message = "支社を選択してください")
    private int branchId;

    @NotNull(message = "部署を選択してください")
    private int departmentId;

    private int isStopped;

    public enum Status {
        復活, 停止
    }

    private Date createdDate;

    private Date updatedDate;
}
