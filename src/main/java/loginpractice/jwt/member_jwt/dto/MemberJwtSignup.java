package loginpractice.jwt.member_jwt.dto;

import loginpractice.jwt.member_jwt.domain.MemberJwt;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtSignup {

    private String name;

    private String password;

    @Builder
    private MemberJwtSignup(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public MemberJwt toEntity() {
        return MemberJwt.builder()
                .name(name)
                .password(password)
                .build();
    }
}
