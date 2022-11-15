package com.scnu.gotravel.dto;

import com.scnu.gotravel.dto.board.ResponseBoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MainDto {

    List<ResponseBoardDto> rank = new ArrayList<>();
    List<HashTagRecommendDto> hashTags = new ArrayList<>();
}
