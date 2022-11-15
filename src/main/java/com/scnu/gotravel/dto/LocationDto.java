package com.scnu.gotravel.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationDto {

    private Long id;
    private String name;
    private Long parentId;
}
