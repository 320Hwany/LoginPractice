package loginpractice.cookie_session.filter.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import loginpractice.cookie_session.filter.dto.MemberLogin;
import loginpractice.cookie_session.filter.repository.MemberRepository;
import loginpractice.cookie_session.filter.domain.MemberSession;
import loginpractice.cookie_session.filter.dto.MemberSignup;
import loginpractice.cookie_session.filter.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
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
                MemberSession memberSession = MemberSession.toMemberSession(member);
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
}
