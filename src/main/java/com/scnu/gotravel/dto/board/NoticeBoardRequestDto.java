package com.scnu.gotravel.dto.board;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoticeBoardRequestDto {

    private Long id;
    private String title;
    private String content;
}
