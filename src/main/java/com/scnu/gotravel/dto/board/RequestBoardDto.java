package com.scnu.gotravel.dto.board;

import lombok.*;

import java.util.List;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequestBoardDto {

    private String title;

    private String content;

    private Long locationNum;

    private List<Long> hashTag;
}