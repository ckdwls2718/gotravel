package com.scnu.gotravel.dto.photographer;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class PhotographerRequestDto {

    private Long id;
    private String content;
    private List<String> imageURL;
    private List<Long> hashtag;
    private Long locationNum;
}
