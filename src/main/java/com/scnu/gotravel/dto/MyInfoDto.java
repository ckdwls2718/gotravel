package com.scnu.gotravel.dto;

import lombok.*;

import java.util.Map;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyInfoDto {

    private String nickname;

    private Map<Long,String> board;
    private Map<Long,Long> boardCount;

    private Map<Long,String> recommend;
    private Map<Long,Long> recommendCount;
}
