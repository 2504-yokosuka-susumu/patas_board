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
public class UserEditController {
    @Autowired
    UserService userService;

    @GetMapping("/setting/form")
    public ModelAndView view(@RequestParam("id") String id) {
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
                                   @RequestParam(name = "confirmPassword", required = false) String confirmPassword) {

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        // Formバリデーションチェック
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/setting");
        } else if (!Objects.equals(userForm.getPassword(), confirmPassword)) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
            mav.setViewName("/setting");
        } else if ((userForm.getBranchId() == 1 || userForm.getBranchId() == 2) && userForm.getDepartmentId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/setting");
        } else if ((userForm.getBranchId() == 3 || userForm.getBranchId() == 4) && userForm.getDepartmentId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/setting");
        } else {

            String account = userForm.getAccount();
            UserForm existAccount = userService.checkedAccount(account);

            // アカウントが重複しているか
            if (existAccount.getId() != userForm.getId()) {
                errorMessages.add("アカウントが重複しています");
                String settingUrl = "/setting/form?id=" + String.valueOf(userForm.getId());
                mav.setViewName("/setting/form");
                mav.addObject("errorMessages", errorMessages);
                return mav;
            }

            // ステータス更新処理
            userService.updateUser(userForm);
            mav.setViewName("redirect:/");
        }

        mav.addObject("errorMessages", errorMessages);
        return mav;
    }
}
