package loginpractice.cookie_session.argument_resolver.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import loginpractice.cookie_session.argument_resolver.annotation.Login;
import loginpractice.cookie_session.member.domain.MemberSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberSessionType = parameter.getParameterType().equals(MemberSession.class);
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(Login.class);
        return hasMemberSessionType && hasLoginMemberAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new IllegalArgumentException("세션이 존재하지 않습니다");
        }

        MemberSession memberSession = (MemberSession) session.getAttribute("MemberSession");

        if (memberSession == null) {
            throw new IllegalArgumentException("MemberSession이 존재하지 않습니다");
        }

        return memberSession;
    }
}
