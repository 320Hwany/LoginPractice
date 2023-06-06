package loginpractice.cookie_session.filter.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSession implements Serializable {

    private Long id;

    private String name;

    private String password;

    @Builder
    private MemberSession(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void makeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("MemberSession", this);
    }

    public static MemberSession toMemberSession(Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .name(member.getName())
                .password(member.getPassword())
                .build();
    }
}
