package com.example.patas_board.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginFilterConfig {
    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        FilterRegistrationBean<LoginFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new LoginFilter());
        //ログイン情報が必要なURL
        // ホーム画面
        bean.addUrlPatterns("/patas_board");
        // 新規投稿画面
        bean.addUrlPatterns("/new");
        // 投稿登録処理
        bean.addUrlPatterns("/add");
        // ユーザー登録画面
        bean.addUrlPatterns("/signup/*");
        // 削除処理
        bean.addUrlPatterns("/delete/*");
        // コメント処理
        bean.addUrlPatterns("/comment/*");
        // 編集画面
        bean.addUrlPatterns("/setting/*");
        // 管理者画面
        bean.addUrlPatterns("/manager/*");
        // 一番最初にかかるフィルターとして指定
        bean.setOrder(1);
        return bean;
    }
}
