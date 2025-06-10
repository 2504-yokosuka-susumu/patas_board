package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.repository.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

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

//    @PostMapping("/login")
//    public ModelAndView login(@ModelAttribute("userForm") @Validated UserForm userForm, BindingResult result){
//
//        if(result.hasErrors()){
//            ModelAndView mav = new ModelAndView();
//            List<String> errorMessages = new ArrayList<String>();
//            for(ObjectError error:result.getAllErrors()){
//                errorMessages.add(error.getDefaultMessage());
//            }
//            mav.setViewName("/login/form");
//            return mav;
//        }else{
//            User user = userService.login();
//        }
//    }
}
