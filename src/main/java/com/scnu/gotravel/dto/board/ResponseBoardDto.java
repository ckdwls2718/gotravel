package com.scnu.gotravel.dto.board;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class ResponseBoardDto {

    private Long id;

    private String title;

    private String content;

    private int recommendCount;

    private String memberId;

    private List<String> imageList;

    private String location;

    private int likeCheck;

    private List<String> board_hashTags;

    private LocalDateTime createDate;

    private String nickName;
}

