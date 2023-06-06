package loginpractice.cookie_session.filter.presentation;

import jakarta.servlet.http.HttpServletRequest;
import loginpractice.cookie_session.filter.domain.Member;
import loginpractice.cookie_session.filter.dto.MemberLogin;
import loginpractice.cookie_session.filter.application.MemberService;
import loginpractice.cookie_session.filter.dto.MemberResponse;
import loginpractice.cookie_session.filter.dto.MemberSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static loginpractice.cookie_session.filter.dto.MemberResponse.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public MemberResponse get(HttpServletRequest request) {
        Member member = memberService.get(request);
        return toMemberResponse(member);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody MemberSignup memberSignup) {
        memberService.save(memberSignup);
    }

    @PostMapping("/login")
    public void login(@RequestBody MemberLogin memberLogin,
                      HttpServletRequest request) {
        memberService.login(memberLogin, request);
    }
}
