package com.example.patas_board.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerFilterConfig {
    @Bean
    public FilterRegistrationBean<ManagerFilter> managerFilter() {
        FilterRegistrationBean<ManagerFilter> bean = new FilterRegistrationBean<>();

        // フィルターをかけるURLの指定
        bean.setFilter(new ManagerFilter());
        // 管理者画面
        bean.addUrlPatterns("/manager/*");
        // 編集画面
        bean.addUrlPatterns("/setting/*");
        // ユーザー登録画面
        bean.addUrlPatterns("/signup/*");
        // 画面がログインフィルターと被っている場合、二番目にかけられる
        bean.setOrder(2);
        return bean;
    }
}
