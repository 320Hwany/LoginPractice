package loginpractice.cookie_session.member.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import loginpractice.cookie_session.member.domain.MemberSession;
import loginpractice.cookie_session.member.dto.MemberLogin;
import loginpractice.cookie_session.member.repository.MemberRepository;
import loginpractice.cookie_session.member.dto.MemberSignup;
import loginpractice.cookie_session.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static loginpractice.cookie_session.member.domain.MemberSession.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(MemberSignup memberSignup) {
        memberRepository.save(memberSignup.toEntity());
    }

    public void login(MemberLogin memberLogin, HttpServletRequest request) {
        Optional<Member> optionalMember = memberRepository.findByName(memberLogin.getName());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (memberLogin.getPassword().equals(member.getPassword())) {
                MemberSession memberSession = toMemberSession(member);
                memberSession.makeSession(request);
            }
        }
    }

    public Member get(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        MemberSession memberSession = (MemberSession) session.getAttribute("MemberSession");
        Optional<Member> optionalMember = memberRepository.findById(memberSession.getId());
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }
        throw new IllegalArgumentException("회원이 존재하지 않습니다");
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
