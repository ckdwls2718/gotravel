package com.scnu.gotravel.dto.member;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRegisterRequestDto {

    private Long id;

    private String name; // 사용자 이름

    private String email; // 이메일

    private String password; // 비밀번호

    private String nickname; // 닉네임

}
