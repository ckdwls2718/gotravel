package com.scnu.gotravel.dto;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LetterDto {

    private Long id;
    private String sendMember;
    private String receiveMember;
    private String title;
    private String content;

}
