package loginpractice.jwt.interceptor_argument_resolver.config;

import jakarta.servlet.http.HttpServletRequest;
import loginpractice.jwt.interceptor_argument_resolver.annotation.JwtLoginV2;
import loginpractice.jwt.member_jwt.domain.MemberJwtSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberJwtArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isMemberJwtSessionType = parameter.getParameterType().equals(MemberJwtSession.class);
        boolean isJwtLoginAnnotation = parameter.hasParameterAnnotation(JwtLoginV2.class);
        return isMemberJwtSessionType && isJwtLoginAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return request.getAttribute("MemberJwtSession");
    }
}
