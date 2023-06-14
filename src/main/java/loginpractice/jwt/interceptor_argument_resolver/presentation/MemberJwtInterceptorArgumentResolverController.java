package loginpractice.jwt.interceptor_argument_resolver.presentation;

import jakarta.servlet.http.HttpServletResponse;
import loginpractice.jwt.interceptor_argument_resolver.annotation.JwtLoginV2;
import loginpractice.jwt.member_jwt.application.MemberJwtService;
import loginpractice.jwt.member_jwt.domain.MemberJwtSession;
import loginpractice.jwt.member_jwt.dto.MemberJwtLogin;
import loginpractice.jwt.member_jwt.dto.MemberJwtSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/jwt/interceptor")
@RestController
public class MemberJwtInterceptorArgumentResolverController {

    private final MemberJwtService memberJwtService;

    @GetMapping("/argumentResolver")
    public MemberJwtSession getMemberJwtSession(@JwtLoginV2 MemberJwtSession memberJwtSession) {
        return memberJwtSession;
    }

    @GetMapping("/argumentResolver-without")
    public void get() {
    }

    @PostMapping("/login")
    public void login(@RequestBody MemberJwtLogin memberJwtLogin,
                      HttpServletResponse response) {
        memberJwtService.login(memberJwtLogin, response);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody MemberJwtSignup memberJwtSignup) {
        memberJwtService.signup(memberJwtSignup);
    }
}
