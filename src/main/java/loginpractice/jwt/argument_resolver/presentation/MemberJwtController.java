package loginpractice.jwt.argument_resolver.presentation;

import jakarta.servlet.http.HttpServletResponse;
import loginpractice.jwt.argument_resolver.annotation.JwtLogin;
import loginpractice.jwt.member_jwt.application.MemberJwtService;
import loginpractice.jwt.member_jwt.domain.MemberJwt;
import loginpractice.jwt.member_jwt.domain.MemberJwtSession;
import loginpractice.jwt.member_jwt.dto.MemberJwtLogin;
import loginpractice.jwt.member_jwt.dto.MemberJwtSignup;
import loginpractice.jwt.member_jwt.dto.MemberJwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
public class MemberJwtController {

    private final MemberJwtService memberJwtService;

    @PostMapping("/signup")
    public void signup(@RequestBody MemberJwtSignup memberJwtSignup) {
        memberJwtService.signup(memberJwtSignup);
    }

    @PostMapping("/login")
    public MemberJwtToken login(@RequestBody MemberJwtLogin memberJwtLogin,
                                HttpServletResponse response) {
        return memberJwtService.login(memberJwtLogin, response);
    }

    @GetMapping("/member-jwt")
    public MemberJwtSession get(@JwtLogin MemberJwtSession memberJwtSession) {
        return memberJwtSession;
    }
}
