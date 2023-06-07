package loginpractice.jwt.member_jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtLogin {

    private String name;

    private String password;

    @Builder
    private MemberJwtLogin(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
