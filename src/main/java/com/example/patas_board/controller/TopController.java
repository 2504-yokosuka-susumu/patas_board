package com.example.patas_board.controller;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.service.CommentService;
import com.example.patas_board.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

@Controller
public class TopController {

    @Autowired
    MessageService messageService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView view() {
        ModelAndView mav = new ModelAndView();

        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得 日付検索に変えた
        List<MessageForm> messageData = messageService.findAllMessage();
        // 返信を全件取得
//        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");

        mav.addObject("formModel", commentsForm);
        mav.addObject("messages", messageData);
//        mav.addObject("comments", commentData);
        return mav;
    }
    /*
     * 投稿内容表示処理
     */
    @PostMapping("/filter")
    public ModelAndView categorize(@RequestParam(value="start", required = false)String start,
                            @RequestParam(value = "end", required = false)String end,
                            HttpServletRequest request) throws ParseException {
        ModelAndView mav = new ModelAndView();
        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得 日付検索に変えた
        List<MessageForm> messageData = messageService.findByCreated_dateMessage(start, end);
        // 返信を全件取得
//        List<CommentForm> commentData = commentService.findAllComment();
        //エラーメッセージを取得
        mav.addObject("mavErrorMessages", session.getAttribute("errorMessages"));
        mav.addObject("messageId", session.getAttribute("messageId"));
        session.invalidate();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("formModel", commentsForm);
        mav.addObject("messages", messageData);
//        mav.addObject("comments", commentData);
        mav.addObject("start", start);
        mav.addObject("end", end);

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
