package com.scnu.gotravel.dto.photographer;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class PhotographerResponseDto {

    private Long id;

    private String content;

    private String email;

    private List<String> photoImage;

    private List<String> hashtag;

    private String location;

    private String nickName;
}
