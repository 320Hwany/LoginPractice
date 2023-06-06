package loginpractice.cookie_session.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import loginpractice.cookie_session.member.domain.MemberSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("preHandle start");
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalArgumentException("세션이 존재하지 않습니다");
        }

        MemberSession memberSession = (MemberSession) session.getAttribute("MemberSession");
        if (memberSession == null) {
            throw new IllegalArgumentException("MemberSession이 존재하지 않습니다");
        }

        log.info("preHandle return true");
        return true;
    }
}
