package com.scnu.gotravel.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class LetterSearchCondition {

    private String receiveMemberNickname;
    private int flag;
}
