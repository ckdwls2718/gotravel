package com.scnu.gotravel.dto;

import lombok.*;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Data
public class MemberDTO {

    private Long id;

    private String name;

    private String nickname;

    private String password;

    private String email;


}
