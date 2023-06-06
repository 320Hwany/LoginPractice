package loginpractice.cookie_session.filter.dto;

import loginpractice.cookie_session.filter.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignup {

    private String name;

    private String password;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .password(password)
                .build();
    }
}
