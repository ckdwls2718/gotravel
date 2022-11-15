package com.scnu.gotravel.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhotographerDTO {

    private Long id;
    private String memberId;
    private String content;
    private List<String> imageURL;
    private List<Long> hashtag;
    private Long locationNum;

}