package com.example.patas_board.controller;

import com.example.patas_board.service.MessageService;
import com.example.patas_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserMessageController {
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    HttpSession session;

    @GetMapping("/manager/form")
    public ModelAndView view() {
        // 管理者権限フィルター

        // session情報取得
        session = (HttpSession) session.getAttribute("loginUser");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/manager");
        return mav;
    }

    /*
     * アカウント復活/停止切り替え
     */
    @PostMapping("/manager")
    public ModelAndView changeStatus(){
        ModelAndView mav = new ModelAndView();

        return mav;
    }
}
