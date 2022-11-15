package com.scnu.gotravel.dto;

import com.scnu.gotravel.dto.board.ResponseBoardDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashTagRecommendDto {

    private String hashTagName;

    private List<ResponseBoardDto> boardDtoList = new ArrayList<>();

}
