package com.scnu.gotravel.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationSearchDto {
    private List<LocationDto> ParentLocationList;
    private List<LocationDto> ChildLocationList;
    private List<HashTagDto> hashTagList;
}
