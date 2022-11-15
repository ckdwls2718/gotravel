package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.MyInfoDto;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.service.BoardService;
import com.scnu.gotravel.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final RecommendService recommendService;
    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<MyInfoDto> getBoard() {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        Map<Long, String> board = new HashMap<>();
        Map<Long, Long> boardCount = new HashMap<>();

        List<ResponseBoardDto> boardList = boardService.findByMemberId(loginMemberEmail);
        for (ResponseBoardDto dto : boardList) {
            board.put(dto.getId(), dto.getImageList().get(0));
            boardCount.put(dto.getId(), (long) dto.getRecommendCount());
        }

        MyInfoDto myInfoDto = MyInfoDto.builder()
                .nickname(loginMemberEmail)
                .board(board)
                .boardCount(boardCount)
                .build();
        return ResponseEntity.ok().body(myInfoDto);
    }

    @GetMapping("/recommend")
    public ResponseEntity<MyInfoDto> getRecommend() {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        Map<Long, String> recommend = new HashMap<>();
        Map<Long, Long> recommendCount = new HashMap<>();

        List<ResponseBoardDto> recommendList = recommendService.findAll();
        for (ResponseBoardDto dto : recommendList) {
            recommend.put(dto.getId(),dto.getImageList().get(0));
            recommendCount.put(dto.getId(), (long) dto.getRecommendCount());
        }


        MyInfoDto myInfoDto = MyInfoDto.builder()
                .nickname(loginMemberEmail)
                .recommend(recommend)
                .recommendCount(recommendCount)
                .build();
        return ResponseEntity.ok().body(myInfoDto);
    }
}
