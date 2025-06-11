package com.example.patas_board.controller;

import ch.qos.logback.core.util.StringUtil;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private HttpSession session;

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
    public ModelAndView login(@ModelAttribute("password") String password, @ModelAttribute("account") String account){
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();
        if(password.isBlank()) {
            errorMessages.add("パスワードを入力してください");
            mav.setViewName("/login");
        }
        if(account.isBlank()) {
            errorMessages.add("アカウントを入力してください");
            mav.setViewName("/login");
        }else if(!account.matches("^[a-zA-Z0-9]{6,20}+$")) {
            errorMessages.add("アカウントは半角英数字かつ6文字以上20文字以下で入力してください");
            mav.setViewName("/login");
        }else{
            UserForm user = userService.login(account, password);
            if(user == null || user.getIsStopped() == 1){
                errorMessages.add("ログインに失敗しました");
                mav.addObject("errorMessages", errorMessages);
                mav.setViewName("/login");
                return mav;
            }
            session.setAttribute("loginUser", user);
            mav.setViewName("redirect:/");
        }
        mav.addObject("errorMessages", errorMessages);
        return mav;
    }
}
