package com.example.patas_board.controller;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.service.CommentService;
import com.example.patas_board.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

        String categoryText = new String();
        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得
        List<UserMessageForm> messageData = messageService.findAllMessage();
        // 返信を全件取得
        List<UserCommentForm> commentData = commentService.findAllComment();
        // セッション情報からエラーメッセージを取得
        List<String> errorMessages = (List<String>) session.getAttribute("errorMessages");
        // エラーメッセージがnullじゃなかったらViewに渡す
        if(errorMessages != null){
            mav.addObject("errorMessages", errorMessages);
        }
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 値を渡したらセッションからエラーメッセージを消す
        session.removeAttribute("errorMessages");

        mav.addObject("categoryText", categoryText);
        mav.addObject("formModel", commentsForm);
        mav.addObject("messages", messageData);
        mav.addObject("comments", commentData);
        return mav;
    }
    /*
     * 投稿内容表示処理
     */
    @GetMapping("/filter")
    public ModelAndView categorize(@RequestParam(value="start", required = false)String start,
                                    @RequestParam(value = "end", required = false)String end,
                                   @RequestParam(value="categoryText", required = false)String categoryText,
                                    HttpServletRequest request) throws ParseException {
        ModelAndView mav = new ModelAndView();
        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得 日付検索に変えた
        List<UserMessageForm> messageData = messageService.findByCreatedDateMessage(start, end, categoryText);
        // 返信を全件取得
        List<UserCommentForm> commentData = commentService.findAllComment();
        //エラーメッセージを取得
        mav.addObject("mavErrorMessages", session.getAttribute("errorMessages"));
        mav.addObject("messageId", session.getAttribute("messageId"));
        session.removeAttribute("errorMessages");
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("formModel", commentsForm);
        mav.addObject("messages", messageData);
        mav.addObject("comments", commentData);
        mav.addObject("start", start);
        mav.addObject("end", end);
        mav.addObject("categoryText", categoryText);

        return mav;
    }

    /*
     * ログアウト処理
     */
    @GetMapping("/logout")
    public ModelAndView logOut() {
        // セッションの無効化
        session.invalidate();
        // rootへリダイレクト
        return new ModelAndView("redirect:/login/form");
    }

}
