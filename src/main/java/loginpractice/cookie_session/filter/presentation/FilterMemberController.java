package loginpractice.cookie_session.filter.presentation;

import jakarta.servlet.http.HttpServletRequest;
import loginpractice.cookie_session.member.application.MemberService;
import loginpractice.cookie_session.member.dto.MemberResponse;
import loginpractice.cookie_session.member.domain.Member;
import loginpractice.cookie_session.member.dto.MemberLogin;
import loginpractice.cookie_session.member.dto.MemberSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static loginpractice.cookie_session.member.dto.MemberResponse.toMemberResponse;

@RequiredArgsConstructor
@RequestMapping("/filter")
@RestController
public class FilterMemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public MemberResponse get(HttpServletRequest request) {
        Member member = memberService.getMemberByRequest(request);
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

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        memberService.logout(request);
    }
}
