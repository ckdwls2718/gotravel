package com.scnu.gotravel.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class MemberSearchCondition {
    String nickname;
    String email;
}
