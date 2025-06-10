package com.example.patas_board.controller;

import com.example.patas_board.controller.form.BranchForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserEditController {
    @Autowired
    UserService userService;

    @GetMapping("/setting/form/{id}")
    public ModelAndView view(@PathVariable String id) {
        // 管理者権限フィルター

        // ユーザ情報取得
        int userId = Integer.parseInt(id);
        UserForm userData = userService.selectUser(userId);

        // "users"オブジェクト "statuses"オブジェクト　格納
        ModelAndView mav = new ModelAndView();
        mav.addObject("users",userData);
        mav.setViewName("/setting");
        return mav;
    }

    /*
     * ユーザ編集更新処理
     */
    @PostMapping("/setting")
    public ModelAndView updateUser(@ModelAttribute("formModel") UserForm userForm) {

        // User情報をFormに格納

        // ステータス更新処理
        userService.updateUser(userForm);

        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
