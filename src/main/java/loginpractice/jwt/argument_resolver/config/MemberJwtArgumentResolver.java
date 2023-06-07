package loginpractice.jwt.argument_resolver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import loginpractice.jwt.argument_resolver.annotation.JwtLogin;
import loginpractice.jwt.member_jwt.domain.MemberJwt;
import loginpractice.jwt.member_jwt.domain.MemberJwtSession;
import loginpractice.jwt.member_jwt.repository.MemberJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;
import java.util.Optional;

import static loginpractice.jwt.member_jwt.JwtKey.JWT_KEY;
import static loginpractice.jwt.member_jwt.domain.MemberJwtSession.*;

@RequiredArgsConstructor
public class MemberJwtArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberJwtRepository memberJwtRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isMemberJwtSessionType = parameter.getParameterType().equals(MemberJwtSession.class);
        boolean isJwtLoginAnnotation = parameter.hasParameterAnnotation(JwtLogin.class);
        return isMemberJwtSessionType && isJwtLoginAnnotation;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        String accessJws = webRequest.getHeader("Access_token");
        byte[] decodedKey = Base64.getDecoder().decode(JWT_KEY);
        return getMemberSessionFromAccessJws(accessJws, decodedKey, webRequest);
    }

    private MemberJwtSession getMemberSessionFromAccessJws(String jws, byte[] decodedKey, NativeWebRequest webRequest) {
        try {
            Jws<Claims> claims = getClaims(jws, decodedKey);
            String memberId = claims.getBody().getSubject();
            Optional<MemberJwt> optionalMemberJwt = memberJwtRepository.findById(Long.valueOf(memberId));
            if (optionalMemberJwt.isPresent()) {
                MemberJwt memberJwt = optionalMemberJwt.get();
                return toMemberJwtSession(memberJwt);
            }
            throw new JwtException("AccessToken이 존재하지 않습니다");
        } catch (JwtException e) {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            Cookie[] cookies = getCookies(request);
            String refreshJws = getRefreshJws(cookies);
            return getMemberSessionFromRefreshJws(refreshJws, decodedKey);
        }
    }

    private static Jws<Claims> getClaims(String jws, byte[] decodedKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jws);
        } catch (IllegalArgumentException e) {
            throw new JwtException("getClaims error");
        }
    }

    private static Cookie[] getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키가 존재하지 않습니다");
        }
        return cookies;
    }

    private static String getRefreshJws(Cookie[] cookies) {
        String refreshJws = cookies[0].getValue();
        if (refreshJws == null || refreshJws.equals("")) {
            throw new IllegalArgumentException("getRefreshJws error");
        }
        return refreshJws;
    }

    private MemberJwtSession getMemberSessionFromRefreshJws(String jws, byte[] decodedKey) {
        try {
            Jws<Claims> claims = getClaims(jws, decodedKey);
            String memberId = claims.getBody().getSubject();
            Optional<MemberJwt> optionalMemberJwt = memberJwtRepository.findById(Long.valueOf(memberId));
            if (optionalMemberJwt.isPresent()) {
                MemberJwt memberJwt = optionalMemberJwt.get();
                return toMemberJwtSession(memberJwt);
            }
            throw new IllegalArgumentException("getMemberSessionFromRefreshJws error");
        } catch (JwtException e) {
            throw new IllegalArgumentException("getMemberSessionFromRefreshJws error");
        }
    }
}
