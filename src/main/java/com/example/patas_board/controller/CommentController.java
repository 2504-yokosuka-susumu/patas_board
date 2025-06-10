package com.example.patas_board.controller;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.service.CommentService;
import com.example.patas_board.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    MessageService messageService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;
    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/{messageId}")
    public String addComment(@Validated @ModelAttribute("formModel") CommentForm commentForm,
                             BindingResult result,
                             @PathVariable Integer messageId){
        if(result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages", errorMessages);
            session.setAttribute("messageId", messageId);
            return "redirect:/";
        }
        // 投稿をテーブルに格納
        commentForm.setMessageId(messageId);
        commentService.saveComment(commentForm);
//        // idをもとにDBから投稿を入手する
//        MessageForm messageForm = messageService.editMessage(messageId);
//        // 投稿のupdated_dateを更新
//        messageService.saveMessage(messageForm);
        // rootへリダイレクト
        return "redirect:/";
    }

}
