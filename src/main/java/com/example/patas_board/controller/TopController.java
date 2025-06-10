package com.example.patas_board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopController {
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();

        // 画面遷移先を指定
        mav.setViewName("/top");
        return mav;
    }

    /*
     * ログアウト処理
     */
    @GetMapping("/logout")
    public ModelAndView logOut() {
        // session情報取得
        session = (HttpSession) session.getAttribute("loginUser");

        // セッションの無効化
        session.invalidate();
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
