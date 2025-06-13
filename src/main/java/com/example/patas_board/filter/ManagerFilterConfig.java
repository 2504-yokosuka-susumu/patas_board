package com.example.patas_board.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerFilterConfig {
    @Bean
    public FilterRegistrationBean<ManagerFilter> managerFilter() {
        FilterRegistrationBean<ManagerFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new ManagerFilter());
        bean.addUrlPatterns("/manager/*");
        bean.addUrlPatterns("/setting/*");
        bean.addUrlPatterns("/signup/*");
        bean.setOrder(2);
        return bean;
    }
}
