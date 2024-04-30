package ru.mooncess.serverjwt.filter;

import ru.mooncess.serverjwt.service.JwtProvider;
import ru.mooncess.serverjwt.service.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mooncess.serverjwt.domain.JwtAuthentication;
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

    private final JwtProvider jwtProvider;

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
//            throws IOException, ServletException {
//        final String token = getTokenFromRequest((HttpServletRequest) request);
//        if (token != null && jwtProvider.validateAccessToken(token)) {
//            final Claims claims = jwtProvider.getAccessClaims(token);
//            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
//            jwtInfoToken.setAuthenticated(true);
//            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
//        }
//        fc.doFilter(request, response);
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        System.out.println("Я в фильтре");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String token = getTokenFromRequest(httpRequest);

        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
            System.out.println(jwtInfoToken.getUsername());
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }

        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        System.out.println("ищу в кукисах");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("В кукисах что-то есть");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    return cookie.getValue();
                }
            }
        }
        System.out.println("Вернул null епт");
        return null;
    }

//    private String getTokenFromRequest(HttpServletRequest request) {
//        final String bearer = request.getHeader(AUTHORIZATION);
//        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }

}
