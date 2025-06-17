package com.example.patas_board.controller;

import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.BranchService;
import com.example.patas_board.service.DepartmentService;
import com.example.patas_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class UserEditController {
    @Autowired
    UserService userService;
    @Autowired
    BranchService branchService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    HttpSession session;
    @Autowired
    public SmartValidator validator;

    @GetMapping("/setting/form")
    public ModelAndView view(@RequestParam("id") String id) {

        List<String> errorMessages = new ArrayList<String>();
        ModelAndView mav = new ModelAndView();

        if (id.isBlank()) {
            errorMessages.add("不正なパラメータが入力されました");
            mav.setViewName("redirect:/manager/form");

        } else if (!id.matches("^[0-9]+$")) {
            errorMessages.add("不正なパラメータが入力されました");
            mav.setViewName("redirect:/manager/form");
        } else {
            // ユーザ情報取得
            int userId = Integer.parseInt(id);
            UserForm userData = userService.selectUser(userId);

            if (userData == null) {
                errorMessages.add("不正なパラメータが入力されました");
                mav.setViewName("redirect:/manager/form");
                session.setAttribute("errorMessages", errorMessages);
                return mav;
            }

            // "users"オブジェクト "statuses"オブジェクト　格納

            HashMap<Integer, String> branchChoices = branchService.findAllBranchesMap();

            //タスクステータスリスト作成
            HashMap<Integer, String> departmentChoices = departmentService.findAllDepartmentsMap();

            // セッションよりデータを取得して設
            mav.addObject("users", userData);
            mav.addObject("branchChoices", branchChoices);
            mav.addObject("departmentChoices", departmentChoices);
            mav.setViewName("/setting");

            return mav;
        }
        session.setAttribute("errorMessages", errorMessages);
        return mav;
    }



    public ModelAndView view(@RequestParam("id") String id, @ModelAttribute("errorMessages") List<String> errorMessages) {
        // ユーザ情報取得
        int userId = Integer.parseInt(id);
        UserForm userData = userService.selectUser(userId);

        // "users"オブジェクト "statuses"オブジェクト　格納
        ModelAndView mav = new ModelAndView();

        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();

        //タスクステータスリスト作成
        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        mav.addObject("errorMessages", errorMessages);
        mav.addObject("users",userData);
        mav.setViewName("/setting");
        mav.addObject("branchChoices", branchChoices);
        mav.addObject("departmentChoices", departmentChoices);
        return mav;
    }

    /*
     * アカウント復活/停止切り替え
     */
    @PostMapping("/setting")
    public ModelAndView updateUser(@ModelAttribute("formModel") UserForm userForm, BindingResult result,
                                   @RequestParam(name = "confirmPassword", required = false) String confirmPassword) {

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        UserForm replaceUserForm = userForm;
        String replaceName = replaceUserForm.getName();
        replaceName = replaceName.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        replaceUserForm.setName(replaceName);
        validator.validate(replaceUserForm, result);
        // Formバリデーションチェック
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        // パスワードPattern,一致しているか、支社と部署の組み合わせ、アカウントが重複しているか
        if(!userForm.getPassword().isBlank() &&!userForm.getPassword().matches("^[!-~]{6,20}+$")){
            errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
        } else if (!Objects.equals(userForm.getPassword(), confirmPassword)) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
        }
        if(userForm.getBranchId() == 0){
            errorMessages.add("支社を入力してください");
        }
        if(userForm.getDepartmentId() == 0){
            errorMessages.add("部署を入力してください");
        } else if ((userForm.getDepartmentId() == 1 || userForm.getDepartmentId() == 2) && userForm.getBranchId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else if ((userForm.getDepartmentId() == 3 || userForm.getDepartmentId() == 4) && userForm.getBranchId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else {

            String account = userForm.getAccount();
            UserForm existAccount = userService.checkedAccount(account);

            // アカウントが重複しているか
            if (existAccount != null && existAccount.getId() != userForm.getId()) {
                errorMessages.add("アカウントが重複しています");
            }

            if(errorMessages.size() == 0) {
                // ステータス更新処理
                userService.updateUser(userForm);
                mav.setViewName("redirect:/manager/form");
                return mav;
            }
        }
        return view(String.valueOf(userForm.getId()), errorMessages);
    }
}
