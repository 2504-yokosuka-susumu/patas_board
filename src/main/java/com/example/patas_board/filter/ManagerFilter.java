package com.example.patas_board.filter;

import com.example.patas_board.controller.form.UserForm;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

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

        session = httpRequest.getSession(false);
        UserForm user = (UserForm) session.getAttribute("loginUser");

        if (user.getDepartmentId() == 1){
            chain.doFilter(httpRequest,httpResponse);
        } else {
            session = httpRequest.getSession(true);
            //エラーメッセージをセット
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add("無効なアクセスです");
            session.setAttribute("errorMessages", errorMessages);
            //ホーム画面にリダイレクト
            httpResponse.sendRedirect("redirect:/");
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}