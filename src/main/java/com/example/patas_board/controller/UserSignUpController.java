package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserSignUpController {

    @Autowired
    UserService userService;

    @GetMapping("/signup/form")
    public ModelAndView view(){
        ModelAndView mav = new ModelAndView();
        UserForm userForm = new UserForm();
        mav.addObject("formModel", userForm);
        mav.setViewName("/signup");
        return mav;
    }

    @PostMapping("/signup")
    public ModelAndView createUser(@ModelAttribute("formModel") @Validated UserForm userForm, BindingResult result,
                                   @ModelAttribute("checkedPassword") String checkedPassword) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/signup");
        }
        if (!Objects.equals(userForm.getPassword(), checkedPassword)) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
            mav.setViewName("/signup");
        }
        if ((userForm.getBranchId() == 1 || userForm.getBranchId() == 2) && userForm.getDepartmentId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else if ((userForm.getBranchId() == 3 || userForm.getBranchId() == 4) && userForm.getDepartmentId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else {

            String account = userForm.getAccount();
            UserForm existAccount = userService.checkedAccount(account);

            if (existAccount != null) {
                errorMessages.add("アカウントが重複しています");
                mav.setViewName("/signup");
                mav.addObject("errorMessages", errorMessages);
                return mav;
            }
            userService.createUser(userForm);
            mav.setViewName("redirect:/manager/form");
        }
        mav.addObject("errorMessages", errorMessages);
        return mav;
    }
}
