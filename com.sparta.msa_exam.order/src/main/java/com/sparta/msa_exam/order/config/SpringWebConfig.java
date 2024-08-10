package com.sparta.msa_exam.order.config;

import com.sparta.msa_exam.order.filter.HeaderPortSettingFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringWebConfig {

    @Bean
    public FilterRegistrationBean<?> headerPortFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new HeaderPortSettingFilter());
        filter.addUrlPatterns("/*");
        return filter;
    }

}
