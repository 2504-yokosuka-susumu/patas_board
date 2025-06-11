package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
     * アカウント復活/停止切り替え
     */
    @PostMapping("/setting")
    public ModelAndView updateUser(@ModelAttribute("formModel") UserForm userForm,
                                   @RequestParam(name = "confirmPassword", required = false) String confirmPassword) {

        // ステータス更新処理
        userService.updateUser(userForm);
        return new ModelAndView("redirect:/");
    }
}
