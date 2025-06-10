package com.example.patas_board.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class CommentForm {

    private int id;

    @NotBlank(message = "メッセージを入力してください")
    @Length(message = "500文字以内で入力してください")
    private String text;

    private int userId;

    private int messageId;

    private Date createdDate;

    private Date updatedDate;
}
