package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.repository.entity.User;
import com.example.patas_board.service.UserService;
import com.example.patas_board.utils.CipherUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private Cipher encrypter;

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

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("userForm") @Validated UserForm userForm, BindingResult result, HttpRequest request){

        if(result.hasErrors()){
            ModelAndView mav = new ModelAndView();
            List<String> errorMessages = new ArrayList<String>();
            for(ObjectError error:result.getAllErrors()){
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/login/form");
            return mav;
        }else{
            ModelAndView mav = new ModelAndView();
            String account = userForm.getAccount();
            String password = userForm.getPassword();
            String encPassword = CipherUtil.encrypt(password);
            UserForm user = userService.login(account, encPassword);
            if(user == null || user.getIsStopped() == 1){
                String error = "ログインに失敗しました";
                mav.addObject("error", error);
                mav.setViewName("/login/form");
                return mav;
            }
            session.setAttribute("loginUser", user);
            mav.setViewName("redirect:/");
            return mav;
        }
    }
}
