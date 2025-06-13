package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.BranchService;
import com.example.patas_board.service.DepartmentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
public class UserSignUpController {

    @Autowired
    UserService userService;

    @Autowired
    BranchService branchService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/signup/form")
    public ModelAndView view(){
        ModelAndView mav = new ModelAndView();
        UserForm userForm = new UserForm();

        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();
        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        mav.addObject("formModel", userForm);
        mav.addObject("branchChoices", branchChoices);
        mav.addObject("departmentChoices", departmentChoices);
        mav.setViewName("/signup");
        return mav;
    }

    @PostMapping("/signup")
    public ModelAndView createUser(@ModelAttribute("formModel") @Validated UserForm userForm, BindingResult result,
                                   @ModelAttribute("checkedPassword") String checkedPassword) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();
        // formのバリデーション結果からエラーメッセージを取得
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/signup");
            // パスワードにNotBlankバリデーションがかけられないので入力チェック
        }
        if (userForm.getPassword().isBlank()) {
            errorMessages.add("パスワードを入力してください");
            mav.setViewName("/signup");
        } else if (!userForm.getPassword().matches("^[!-~]{6,20}+$")) {
            errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
            mav.setViewName("/signup");
        }
//        if (!userForm.getAccount().isBlank() && !userForm.getAccount().matches("^[a-zA-Z0-9]{6,20}+$")){
//            errorMessages.add("アカウントは半角英数字かつ6文字以上20文字以下で入力してください");
//        mav.setViewName("/signup");
//        }
        // パスワードと確認用パスワードの一致チェック
        if (!Objects.equals(userForm.getPassword(), checkedPassword)) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
            mav.setViewName("/signup");
        }
        // 支社と部署の組み合わせチェック（departmentIdが本社であるときとないときでバリデーション）
        if ((userForm.getDepartmentId() == 1 || userForm.getDepartmentId() == 2) && userForm.getBranchId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/signup");
        } else if ((userForm.getDepartmentId() == 3 || userForm.getDepartmentId() == 4) && userForm.getBranchId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/signup");
        }
        String account = userForm.getAccount();
            // アカウント情報でアカウントを探しに行く
        UserForm existAccount = userService.checkedAccount(account);
            // アカウントの存在確認ができたら重複しているのでバリデーション
        if (existAccount != null) {
            errorMessages.add("アカウントが重複しています");
            mav.setViewName("/signup");
        }
        if (errorMessages.size() == 0){
            // すべてのバリデーションに引っかからなければアカウントを登録
            userService.createUser(userForm);
            mav.setViewName("redirect:/manager/form");
            return mav;
        }
        mav.addObject("errorMessages", errorMessages);
        return mav;
    }
}
