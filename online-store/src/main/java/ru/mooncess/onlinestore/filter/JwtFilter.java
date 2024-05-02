package ru.mooncess.onlinestore.filter;

import ru.mooncess.onlinestore.domain.JwtAuthentication;
import ru.mooncess.onlinestore.service.AuthService;
import ru.mooncess.onlinestore.service.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final AuthService authService;

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        final String token = getTokenFromRequest((HttpServletRequest) servletRequest);
//        if (token != null && authService.validateToken(token)) {
//            final Claims claims = authService.getClaims(token);
//            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
//            jwtInfoToken.setAuthenticated(true);
//            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        final String bearer = request.getHeader(AUTHORIZATION);
//        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String token = getTokenFromRequest(httpRequest);

        if (token != null && authService.validateToken(token)) {
            final Claims claims = authService.getClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
            System.out.println(jwtInfoToken.getUsername());
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }

        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("В кукис что-то есть");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    System.out.println(cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        System.out.println("again null");
        return null;
    }
}


