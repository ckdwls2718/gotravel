package com.scnu.gotravel.dto.member;


import lombok.*;


@NoArgsConstructor
@Getter
@Builder
@ToString
@AllArgsConstructor
// 여기서 생각해야할 점 1. 어노테이션의 필요에 의한 사용
public class MemberRegisterResponseDto {

    private Long id;

    private String email;

    private String name;

    private String nickname;

}
