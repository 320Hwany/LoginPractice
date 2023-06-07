package loginpractice.jwt.member_jwt.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtSession {

    private Long id;

    private String name;

    private String password;

    private String refreshToken;

    @Builder
    private MemberJwtSession(Long id, String name, String password, String refreshToken) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public static MemberJwtSession toMemberJwtSession(MemberJwt memberJwt) {
        return MemberJwtSession.builder()
                .id(memberJwt.getId())
                .name(memberJwt.getName())
                .password(memberJwt.getPassword())
                .refreshToken(memberJwt.getRefreshToken())
                .build();
    }
}
