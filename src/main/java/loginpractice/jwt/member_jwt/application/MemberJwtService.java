package loginpractice.jwt.member_jwt.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import loginpractice.jwt.member_jwt.domain.MemberJwt;
import loginpractice.jwt.member_jwt.dto.MemberJwtLogin;
import loginpractice.jwt.member_jwt.dto.MemberJwtSignup;
import loginpractice.jwt.member_jwt.dto.MemberJwtToken;
import loginpractice.jwt.member_jwt.repository.MemberJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static loginpractice.jwt.member_jwt.JwtKey.JWT_KEY;
import static loginpractice.jwt.member_jwt.dto.MemberJwtToken.toMemberJwtToken;

@RequiredArgsConstructor
@Service
public class MemberJwtService {

    private final MemberJwtRepository memberJwtRepository;

    @Transactional
    public void signup(MemberJwtSignup memberJwtSignup) {
        memberJwtRepository.save(memberJwtSignup.toEntity());
    }

    @Transactional
    public MemberJwtToken login(MemberJwtLogin memberJwtLogin, HttpServletResponse response) {
        Optional<MemberJwt> optionalMemberJwt = memberJwtRepository.findByName(memberJwtLogin.getName());
        if (optionalMemberJwt.isPresent()) {
            MemberJwt memberJwt = optionalMemberJwt.get();
            if (memberJwt.getPassword().equals(memberJwtLogin.getPassword())) {
                String accessToken = getAccessToken(memberJwt.getId());
                String refreshToken = getRefreshToken(memberJwt.getId());
                memberJwt.updateRefreshToken(refreshToken);
                makeCookie(response, refreshToken);
                return toMemberJwtToken(accessToken, refreshToken);
            }
        }
        throw new IllegalArgumentException("회원 정보가 일치하지 않습니다");
    }

    public void makeCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("Refresh_token", refreshToken)
                .maxAge(Duration.ofDays(30))
//                .httpOnly(true)
//                .secure(true)
                .path("/")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static String getAccessToken(Long memberJwtId) {
        return getToken(memberJwtId, 1000 * 60 * 60); // 1 hour
    }

    public static String getRefreshToken(Long memberJwtId) {
        return getToken(memberJwtId, 1000L * 60 * 60 * 24 * 30); // 30 days
    }

    private static String getToken(Long memberJwtId, long expired) {
        SecretKey tokenKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(JWT_KEY));
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expired);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(String.valueOf(memberJwtId))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(tokenKey)
                .compact();
    }
}
