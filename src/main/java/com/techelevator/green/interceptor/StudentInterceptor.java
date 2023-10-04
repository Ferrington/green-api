package com.techelevator.green.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class StudentInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println(request.getRequestURI()); // "/api/student/1"
        System.out.println(request.getMethod()); // "GET"

        if (request.getUserPrincipal() != null) {
            System.out.println(request.getUserPrincipal().getName());
        }
    }
}
