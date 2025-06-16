package com.example.patas_board.controller;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.service.CommentService;
import com.example.patas_board.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    public SmartValidator validator;
    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/{messageId}/{userId}")
    public ModelAndView addComment(@ModelAttribute("formModel") CommentForm commentForm,
                             BindingResult result,
                             @PathVariable Integer messageId,@PathVariable Integer userId){

        CommentForm replaceCommentForm = commentForm;
        String replaceText = replaceCommentForm.getText();

        replaceText = replaceText.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        replaceCommentForm.setText(replaceText);

        validator.validate(replaceCommentForm, result);

        if(result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages", errorMessages);
            session.setAttribute("messageId", messageId);
            return new ModelAndView("redirect:/patas_board");
        }
        // 投稿をテーブルに格納
        commentForm.setMessageId(messageId);
        commentForm.setUserId(userId);
        commentService.saveComment(commentForm);
//        // idをもとにDBから投稿を入手する
//        MessageForm messageForm = messageService.editMessage(messageId);
//        // 投稿のupdated_dateを更新
//        messageService.saveMessage(messageForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/patas_board");
    }

    /*
     * 返信削除処理
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/patas_board");
    }

}
