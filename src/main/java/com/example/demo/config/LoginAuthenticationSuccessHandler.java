package com.example.demo.config;

import com.example.demo.model.Roles;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException {
        if (!httpServletResponse.isCommitted()){
            String url = getUrl(authentication);
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, url);
        }

        clearAuthenticationAttributes(httpServletRequest);
    }

    private String getUrl(Authentication authentication){
        return authentication.getAuthorities()
                .stream().filter(a -> a.getAuthority().startsWith("ROLE_"))
                .map(s -> Roles.valueOf(s.getAuthority()).getUrl())
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
