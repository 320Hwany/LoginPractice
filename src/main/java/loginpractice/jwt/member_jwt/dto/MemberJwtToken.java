package loginpractice.jwt.member_jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtToken {

    private String accessToken;

    private String refreshToken;

    @Builder
    private MemberJwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static MemberJwtToken toMemberJwtToken(String accessToken, String refreshToken) {
        return MemberJwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
