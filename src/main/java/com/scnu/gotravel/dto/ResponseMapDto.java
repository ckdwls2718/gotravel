package com.scnu.gotravel.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMapDto {
    private String name;
    private long count;
    private String parentName;
}
