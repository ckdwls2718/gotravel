package com.scnu.gotravel.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class NoticeBoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String memberId;
    private LocalDateTime createDate;
}
