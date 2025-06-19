package com.example.patas_board.controller;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.repository.entity.Message;
import com.example.patas_board.service.CommentService;
import com.example.patas_board.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @GetMapping("/patas_board")
    public ModelAndView view(Pageable pageable) {
        ModelAndView mav = new ModelAndView();

        String categoryText = new String();
        // 返信form用の空のCommentForm型を準備
        CommentForm commentsForm = new CommentForm();

        // ページ情報取得
        Page<Message> page = messageService.findPages(pageable);
        // ページ型のコンテンツをUserMessageForm型に変換して表示件数分投稿取得
        List<UserMessageForm> messageData = messageService.setUserMessageForm(page);
        //ページ表示のためのリストを作成
        List<Integer> pageList = messageService.pageList(pageable.getPageNumber()+1, page.getTotalPages());

        // 投稿を全件取得
        //List<UserMessageForm> messageData = messageService.findAllMessage();
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

        mav.addObject("page", page);
        mav.addObject("pageList", pageList);
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
                                   @RequestParam(value = "page",required = false) String pageNum,
                                   Pageable pageable) throws ParseException {
        if(pageNum != null){
            pageable = PageRequest.of(Integer.parseInt(pageNum),2);
        }
        ModelAndView mav = new ModelAndView();
        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得 日付検索に変えた
        List<UserMessageForm> messageAllData = messageService.findByCreatedDateMessage(start, end, categoryText);

        //ページ表示のためのリストを作成
        //lastPageは切り上げの割り算をしている
        int lastPage = (messageAllData.size() + pageable.getPageSize() - 1)/pageable.getPageSize();
        List<Integer> pageList = messageService.pageList(pageable.getPageNumber()+1, lastPage);

        List<UserMessageForm> messagePageData = messageService.getPageData(messageAllData, pageable);

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
        mav.addObject("messages", messagePageData);
        mav.addObject("comments", commentData);
        mav.addObject("pageList", pageList);
        mav.addObject("pageable", pageable);
        mav.addObject("lastPage", lastPage);
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
