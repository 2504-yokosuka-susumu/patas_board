package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
     * アカウント復活/停止切り替え
     */
    @PostMapping("/setting")
    public ModelAndView updateUser(@ModelAttribute("formModel") @Validated UserForm userForm, BindingResult result,
                                   @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
                                   RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        // Formバリデーションチェック
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/setting");
            return mav;
        }
        if(userForm.getPassword() != null &&!userForm.getPassword().matches("^[!-~]{6,20}+$")){
            errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
            mav.setViewName("/signup");
        }
        // パスワードが一致しているか
        if(userForm.getPassword().equals(confirmPassword)) {
            // ステータス更新処理
            userService.updateUser(userForm);
            mav.setViewName("redirect:/");
        } else {
            // エラーメッセージ格納
            String error = "パスワードと確認用パスワードが一致しません";
            redirectAttributes.addFlashAttribute("errorMessage", error);
            mav.setViewName("/setting");
        }

        return mav;
    }
}
