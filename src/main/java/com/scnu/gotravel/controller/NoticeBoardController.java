package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.board.NoticeBoardRequestDto;
import com.scnu.gotravel.dto.board.NoticeBoardResponseDto;
import com.scnu.gotravel.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    @PostMapping(value ="/",  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveNoticeBoard (@RequestPart("dto") NoticeBoardRequestDto noticeBoardRequestDto){
        log.info("dto : " + noticeBoardRequestDto);
        Long aLong = noticeBoardService.NoticeSave(noticeBoardRequestDto);
        return ResponseEntity.ok().body(aLong);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NoticeBoardResponseDto> findOneNotice(@PathVariable("id") Long id){
        log.info("id : " + id);
        NoticeBoardResponseDto noticeBoard=noticeBoardService.findNoticeOne(id);
        return ResponseEntity.ok().body(noticeBoard);
    }

    @GetMapping("/list")
    public ResponseEntity<List<NoticeBoardResponseDto>> NoticeBoardList(){
        List<NoticeBoardResponseDto> noticeBoardList = noticeBoardService.findAllNotice();
        return ResponseEntity.ok().body(noticeBoardList);
    }

    @DeleteMapping("/{id}")
    public HttpStatus NoticeDelete(@PathVariable("id")Long id){
        log.info("id : " + id);
        noticeBoardService.NoticeDelete(id);
        return HttpStatus.OK;
    }
}
