package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.HashTagRecommendDto;
import com.scnu.gotravel.dto.MainDto;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.service.BoardService;
import com.scnu.gotravel.service.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BoardService boardService;
    private final HashTagService hashTagService;

    @GetMapping()
    public ResponseEntity<MainDto> home(int count){

        List<ResponseBoardDto> rankDtoList = boardService.findByRank();

        List<HashTagRecommendDto> hashTagDtoList = boardService.findByHashTag(hashTagService.recommendHashTags(count));

        MainDto mainDto = MainDto.builder()
                .rank(rankDtoList)
                .hashTags(hashTagDtoList)
                .build();

        return ResponseEntity.ok(mainDto);
    }
}
