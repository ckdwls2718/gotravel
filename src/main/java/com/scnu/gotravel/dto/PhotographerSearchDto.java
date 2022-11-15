package com.scnu.gotravel.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhotographerSearchDto {
    private List<LocationDto> ParentLocationList;
    private List<LocationDto> ChildLocationList;
    private List<PhotographerHashTagSearchDto> photographerHashTagSearchList;
}
