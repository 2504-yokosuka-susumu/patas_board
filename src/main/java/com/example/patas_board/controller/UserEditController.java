package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserEditController {
    @Autowired
    UserService userService;

    @GetMapping("/setting/form")
    public ModelAndView view(@PathVariable("id") int id) {
        // 管理者権限フィルター

        // ユーザ情報取得
        UserForm userForm = new UserForm();
        List<UserForm> userData = userService.findAllUser();

        // "users"オブジェクト "statuses"オブジェクト　格納
        ModelAndView mav = new ModelAndView();
        mav.addObject("users",userData);
        mav.setViewName("/edit");
        return mav;
    }
}
