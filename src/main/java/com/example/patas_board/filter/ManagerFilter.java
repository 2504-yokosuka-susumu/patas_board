package com.example.patas_board.filter;

import com.example.patas_board.controller.form.UserForm;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagerFilter implements Filter {
    @Autowired
    HttpSession session;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        //型変換
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        // 元のセッションを使う(新しいセッションを作成していない)
        session = httpRequest.getSession(false);
        // ログインユーザー情報を取得
        UserForm user = (UserForm) session.getAttribute("loginUser");
        // ログインユーザーの部署が総務人事部の場合フィルターを外す
        if (user.getDepartmentId() == 1){
            chain.doFilter(httpRequest,httpResponse);
        // 総務人事部以外の場合はエラーメッセージを表示し、アクセスができない
        } else {
            //session = httpRequest.getSession(true);
            // エラーメッセージのリストを作成
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add("無効なアクセスです");
            // エラーメッセージをセッションに格納(ホーム画面にエラー文を表示するため)
            httpRequest.getSession().setAttribute("errorMessages", errorMessages);
            // ホーム画面にリダイレクト
            httpRequest.getRequestDispatcher("/patas_board").forward(request, response);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}