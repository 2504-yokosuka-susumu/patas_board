package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.BranchService;
import com.example.patas_board.service.DepartmentService;
import com.example.patas_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    public SmartValidator validator;

    /*
     ユーザー登録画面の表示
     */
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

    /*
     新規ユーザー登録処理
     */
    @PostMapping("/signup")
    public ModelAndView createUser(@ModelAttribute("formModel") UserForm userForm, BindingResult result,
                                   @ModelAttribute("checkedPassword") String checkedPassword) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        // 置き換えるためのフォームを定義する
        UserForm replaceUserForm = userForm;
        // 置き換える氏名を定義する
        String replaceName = replaceUserForm.getName();
        // 全角スペースをバリデーションに引っ掛けるために空白に置き換える
        replaceName = replaceName.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        // 置き換え後の氏名をセットする
        replaceUserForm.setName(replaceName);
        // 必要な置き換え終了後にバリデーションを行う
        validator.validate(replaceUserForm, result);
        // formのバリデーション結果からエラーメッセージを取得
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/signup");
        }
        // パスワードは入力しなくても編集できるようにしたいのでif文で入力チェック
        if (userForm.getPassword().isBlank()) {
            errorMessages.add("パスワードを入力してください");
            mav.setViewName("/signup");
        } else if (!userForm.getPassword().matches("^[!-~]{6,20}+$")) {
            errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
            mav.setViewName("/signup");
        }
        // パスワードと確認用パスワードの一致チェック
        if (!Objects.equals(userForm.getPassword(), checkedPassword)) {
            errorMessages.add("入力したパスワードと確認用パスワードが一致しません");
            mav.setViewName("/signup");
        }
        // 支社と部署はnullでバリデーションがかけられないのでif文で必須チェック
        if (userForm.getBranchId() == 0) {
            errorMessages.add("支社を入力してください");
            mav.setViewName("/signup");
        }
        if (userForm.getDepartmentId() == 0){
            errorMessages.add("部署を入力してください");
            mav.setViewName("/signup");
        // 支社と部署の組み合わせチェック（BranchIdが本社であるときとないときでバリデーション）
        } else if ((userForm.getDepartmentId() == 1 || userForm.getDepartmentId() == 2) && userForm.getBranchId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/signup");
        } else if ((userForm.getDepartmentId() == 3 || userForm.getDepartmentId() == 4) && userForm.getBranchId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
            mav.setViewName("/signup");
        }
        // 登録画面で入力されたアカウント名を取得
        String account = userForm.getAccount();
        // アカウント情報でアカウントを探しに行く
        UserForm existAccount = userService.checkedAccount(account);
        // アカウントの存在確認ができたら重複しているのでバリデーション
        if (existAccount != null) {
            errorMessages.add("アカウントが重複しています");
            mav.setViewName("/signup");
        }
        // すべてのバリデーションに引っかからなければアカウントを登録
        if (errorMessages.size() == 0){
            userService.createUser(userForm);
            mav.setViewName("redirect:/manager/form");
            return mav;
        }
        // バリデーションに引っかかって画面が再表示されても支社と部署を表示できるようにDBから情報取得
        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();
        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        // エラーメッセージのリストを画面に渡す
        mav.addObject("errorMessages", errorMessages);
        // 取得した支社と部署の情報を画面に渡す
        mav.addObject("branchChoices", branchChoices);
        mav.addObject("departmentChoices", departmentChoices);
        return mav;
    }
}
