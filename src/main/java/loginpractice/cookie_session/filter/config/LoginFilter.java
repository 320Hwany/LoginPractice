package loginpractice.cookie_session.filter.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import loginpractice.cookie_session.filter.domain.MemberSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LoginFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("LoginFilter doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new IllegalArgumentException("세션이 존재하지 않습니다");
        }

        MemberSession memberSession = (MemberSession) session.getAttribute("MemberSession");
        if (memberSession == null) {
            throw new IllegalArgumentException("MemberSession이 존재하지 않습니다");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
