package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/board")
public class BoardAdminController {
    private final BoardService boardService;

    @GetMapping()
    public ResponseEntity<Page<ResponseBoardDto>> findAll(BoardSearchCondition condition, Pageable pageable){
        log.info("BoardSearchCondition : ",condition);
        log.info("Pageable : ",pageable);
        Page<ResponseBoardDto> boardDtoList = boardService.findByQuerydsl(condition, pageable);

        return ResponseEntity.ok(boardDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBoardDto> findOne(@PathVariable("id") Long boardId) {
        log.info("boardId : ", boardId);
        ResponseBoardDto boardDto = boardService.findOne(boardId);
        return ResponseEntity.ok(boardDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") Long boardId){
        boardService.delete(boardId);
        log.info("boardId : ",boardId);
        return HttpStatus.OK;
    }
}
