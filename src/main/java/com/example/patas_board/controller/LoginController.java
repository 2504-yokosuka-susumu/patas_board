package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @GetMapping("/login/form")
    public ModelAndView view(){
            ModelAndView mav = new ModelAndView();
            // form用の空のentityを準備
            UserForm userForm = new UserForm();
            // 画面遷移先を指定
            mav.setViewName("/login");
            // 準備した空のFormを保管
            mav.addObject("userForm", userForm);
            return mav;
    }
}
