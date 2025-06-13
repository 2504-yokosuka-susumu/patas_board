package com.example.patas_board.controller;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.BranchService;
import com.example.patas_board.service.DepartmentService;
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
    BranchService branchService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    HttpSession session;

    @GetMapping("/manager/form")
    public ModelAndView view() {
        // 管理者権限フィルター

        ModelAndView mav = new ModelAndView();

        // ユーザ情報取得
        List<UserForm> userData = userService.findAllUser();

        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();

        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        List<String> errorMessages = (List<String>)session.getAttribute("errorMessages");

        if(errorMessages != null){
            mav.addObject("errorMessages", errorMessages);
        }
        session.removeAttribute("errorMessages");
        // "users"オブジェクト "statuses"オブジェクト　格納
        mav.addObject("users",userData);
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
