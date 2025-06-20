package com.example.patas_board.controller;

import com.example.patas_board.controller.form.BranchForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.BranchService;
import com.example.patas_board.service.DepartmentService;
import com.example.patas_board.service.MessageService;
import com.example.patas_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Controller
public class UserManageController {
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    BranchService branchService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    HttpSession session;

    /*
     * ユーザ管理画面表示処理
     */
    @GetMapping("/manager/form")
    public ModelAndView view() {

        // mav定義
        ModelAndView mav = new ModelAndView();

        // ユーザ情報取得
        List<UserForm> userData = userService.findAllUser();
        // 支社情報の取得（支社名と投稿数・コメント数を紐づけるため）
        List<BranchForm> branchData = branchService.findAllBranch();

        // Mapで支社名と部署名取得
        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();
        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        // エラーメッセージ取得
        List<String> errorMessages = (List<String>)session.getAttribute("errorMessages");
        if(errorMessages != null){
            mav.addObject("errorMessages", errorMessages);
        }
        session.removeAttribute("errorMessages");
        // ログインユーザーの最終ログイン日時は「ログイン中です」と表示
        String loginText = "ログイン中です";
        // mavにオブジェクト格納してreturnで返す
        mav.addObject("users",userData);
        mav.addObject("branches", branchData);
        mav.addObject("loginText", loginText);
        mav.addObject("statuses", UserForm.Status.values());
        mav.addObject("branchChoices", branchChoices);
        mav.addObject("departmentChoices", departmentChoices);
        mav.setViewName("/manager");

        return mav;
    }

    /*
     * アカウント復活/停止切り替え
     */
    @PostMapping("/manager")
    public ModelAndView changeStatus(@RequestParam(name = "id", required = false) int id,
                                     @RequestParam(name = "status", required = false) int status) {

        // userのidとstatusセット
        UserForm userForm = new UserForm();
        userForm.setId(id);
        userForm.setIsStopped(status);

        // ステータス更新処理
        userService.save(userForm);

        // rootへリダイレクト
        return new ModelAndView("redirect:/patas_board");
    }
}
