package com.example.patas_board.controller;

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

    /*
     * ユーザ編集画面表示処理
     */
    @GetMapping("/setting/form")
    public ModelAndView view(@RequestParam("id") String id) {

        // エラーメッセージのリスト、mav定義
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        // バリデーションチェック (URLチェック)　null/半角数字以外/存在しないID
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

            // 存在しないidをURLで直打ちされた場合
            if (userData == null) {
                errorMessages.add("不正なパラメータが入力されました");
                mav.setViewName("redirect:/manager/form");
                session.setAttribute("errorMessages", errorMessages);
                return mav;
            }

            // Mapで支社名と部署名取得
            HashMap<Integer, String> branchChoices = branchService.findAllBranchesMap();
            HashMap<Integer, String> departmentChoices = departmentService.findAllDepartmentsMap();

            // mavにオブジェクト格納してreturnで返す
            mav.addObject("users", userData);
            mav.addObject("branchChoices", branchChoices);
            mav.addObject("departmentChoices", departmentChoices);
            mav.setViewName("/setting");

            return mav;
        }

        // エラーメッセージをセッションに格納する
        session.setAttribute("errorMessages", errorMessages);
        return mav;
    }

    /*
     * ユーザ編集画面表示処理(更新処理でエラーが発生した場合での処理)
     */
    public ModelAndView view(@RequestParam("id") String id,
                             @ModelAttribute("errorMessages") List<String> errorMessages) {

        // ユーザ情報取得
        int userId = Integer.parseInt(id);
        UserForm userData = userService.selectUser(userId);

        // mav定義
        ModelAndView mav = new ModelAndView();

        // Mapで支社名と部署名取得
        HashMap<Integer,String> branchChoices= branchService.findAllBranchesMap();
        HashMap<Integer,String> departmentChoices= departmentService.findAllDepartmentsMap();

        // mavにオブジェクト格納してreturnで返す
        mav.addObject("errorMessages", errorMessages);
        mav.addObject("users",userData);
        mav.addObject("branchChoices", branchChoices);
        mav.addObject("departmentChoices", departmentChoices);
        mav.setViewName("/setting");
        return mav;
    }

    /*
     * ユーザ編集更新処理
     */
    @PostMapping("/setting")
    public ModelAndView updateUser(@ModelAttribute("formModel") UserForm userForm, BindingResult result,
                                   @RequestParam(name = "confirmPassword", required = false) String confirmPassword) {

        // mav、エラーメッセージリスト定義
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<String>();

        // バリデーションチェック　全角スペース
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
        if ((userForm.getDepartmentId() == 1 || userForm.getDepartmentId() == 2) && userForm.getBranchId() != 1){
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else if ((userForm.getDepartmentId() == 3 || userForm.getDepartmentId() == 4) && userForm.getBranchId() == 1) {
            errorMessages.add("支社と部署の組み合わせが不正です");
        } else {

            // アカウント名をもとに情報取得
            String account = userForm.getAccount();
            UserForm existAccount = userService.checkedAccount(account);

            // アカウントが重複しているか
            if (existAccount != null && existAccount.getId() != userForm.getId()) {
                errorMessages.add("アカウントが重複しています");
            }

            // エラーが一つもなければステータス更新処理
            if(errorMessages.size() == 0) {
                userService.updateUser(userForm);
                mav.setViewName("redirect:/manager/form");
                return mav;
            }
        }

        // ユーザ編集画面のID、エラーメッセージを詰めてviewを呼び出す
        return view(String.valueOf(userForm.getId()), errorMessages);
    }
}
