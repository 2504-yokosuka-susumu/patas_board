package com.example.patas_board.controller;

import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.service.MessageService;
import com.example.patas_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    private HttpSession session;

    @Autowired
    public SmartValidator validator;

    /*
     新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView view(){
        ModelAndView mav = new ModelAndView();
        MessageForm messageForm = new MessageForm();
        // 空のフォームを渡す
        mav.addObject("formModel", messageForm);
        mav.setViewName("/new");
        return mav;
    }

    /*
     新規投稿登録
     */
    @PostMapping("/add")
    public ModelAndView addMessage(@ModelAttribute("formModel") MessageForm messageForm,
                                   BindingResult result, @ModelAttribute("userId") String userId) {

        // 置き換えるフォームの定義
        MessageForm replaceMessageForm = messageForm;
        // 置き換え用テキストの取得
        String replaceText = replaceMessageForm.getText();
        // 置き換え用タイトルの取得
        String replaceTitle = replaceMessageForm.getTitle();
        // 置き換え用カテゴリの取得
        String replaceCategory = replaceMessageForm.getCategory();
        // 全角スペースを空白に置き換え後、置き換えたものをセット
        replaceText = replaceText.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        replaceMessageForm.setText(replaceText);
        replaceTitle = replaceTitle.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        replaceMessageForm.setTitle(replaceTitle);
        replaceCategory = replaceCategory.replaceFirst("^[\\s　]+", "").replaceFirst("[\\s　]+$", "");
        replaceMessageForm.setCategory(replaceCategory);
        //  必要な置き換え終了後、バリデーション
        validator.validate(replaceMessageForm, result);
        // デフォルトのエラーメッセージをリストに詰める
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            List<String> errorMessages = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            // 新規投稿画面に遷移する
            mav.setViewName("/new");
            return mav;
        // エラーがなかったら投稿を登録しにいく
        } else {
            // 画面から受け取ったユーザーIDをフォームに詰める
            messageForm.setUserId(Integer.parseInt(userId));
            messageService.addMessage(messageForm);
            // トップ画面にリダイレクト
            return new ModelAndView("redirect:/patas_board");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Integer id, @ModelAttribute("userId") String userId){

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();

        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        // ログインユーザーIDと投稿のユーザーIDが一致していない場合次の処理へ進む
        if(loginUser.getId() != Integer.parseInt(userId)) {
            // ログインユーザーが情報管理部でない場合次に進む
            if(loginUser.getDepartmentId() != 2) {
                // 投稿のユーザーIDで投稿者の情報を取得
                UserForm postedUser = userService.selectUser(Integer.parseInt(userId));
                // 投稿者の支社とログインユーザーの支社が同じかつ投稿者が技術部でログインユーザーが営業部の場合削除
                if ((postedUser.getBranchId() == loginUser.getBranchId()) && (postedUser.getDepartmentId() == 4 && loginUser.getDepartmentId() == 3)) {
                    // 削除処理
                    messageService.delete(id);
                    // ホーム画面にリダイレクト
                    mav.setViewName("redirect:/patas_board");
                // そうでない場合削除権限なし
                } else {
                    errorMessages.add("削除する権限がありません");
                    // ホーム画面にリダイレクト
                    mav.setViewName("redirect:/patas_board");
                    // ホーム画面でエラー表示するためセッションに格納
                    session.setAttribute("errorMessages", errorMessages);
                }
                return mav;
            }
        }
        // ログインユーザーIDと投稿のユーザーIDが一致またはログインユーザーが情報管理部の場合削除処理
        messageService.delete(id);
        // ホーム画面にリダイレクト
        return new ModelAndView("redirect:/patas_board");
    }
}
