package com.sparta.msa_exam.order.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderPortSettingFilter implements Filter {
    @Value("${server.port}")
    private int port;

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Server-Port", String.valueOf(port));
        chain.doFilter(request, response);
    }
}
