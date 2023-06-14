package loginpractice.jwt.interceptor_argument_resolver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loginpractice.jwt.member_jwt.domain.MemberJwt;
import loginpractice.jwt.member_jwt.domain.MemberJwtSession;
import loginpractice.jwt.member_jwt.repository.MemberJwtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.Optional;

import static loginpractice.jwt.member_jwt.JwtKey.JWT_KEY;
import static loginpractice.jwt.member_jwt.domain.MemberJwtSession.toMemberJwtSession;

@Slf4j
@RequiredArgsConstructor
public class MemberJwtInterceptor implements HandlerInterceptor {

    private final MemberJwtRepository memberJwtRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("preHandle start");
        String accessToken = request.getHeader("Access_token");
        byte[] decodedKey = Base64.getDecoder().decode(JWT_KEY);
        MemberJwtSession memberJwtSession = getMemberSessionFromAccessJws(accessToken, decodedKey, request);
        request.setAttribute("MemberJwtSession", memberJwtSession);
        return true;
    }

    private MemberJwtSession getMemberSessionFromAccessJws(String jws, byte[] decodedKey, HttpServletRequest request) {
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
