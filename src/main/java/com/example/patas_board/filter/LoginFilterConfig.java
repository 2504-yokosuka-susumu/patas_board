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
        bean.addUrlPatterns("/new");
        bean.addUrlPatterns("/add");
        bean.addUrlPatterns("/signup/*");
        bean.addUrlPatterns("/delete/*");
        bean.addUrlPatterns("/comment/*");
        bean.addUrlPatterns("/setting/*");
        bean.addUrlPatterns("/manager/*");
        bean.setOrder(1);
        return bean;
    }
}
