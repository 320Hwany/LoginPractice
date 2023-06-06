package loginpractice.cookie_session.filter.dto;

import loginpractice.cookie_session.filter.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String name;

    @Builder
    private MemberResponse(String name) {
        this.name = name;
    }

    public static MemberResponse toMemberResponse(Member member) {
        return MemberResponse.builder()
                .name(member.getName())
                .build();
    }
}
