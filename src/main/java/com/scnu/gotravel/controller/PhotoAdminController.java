package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.photographer.PhotographerResponseDto;
import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;
import com.scnu.gotravel.service.PhotographerService;
import com.scnu.gotravel.service.ResponseService;
import com.scnu.gotravel.service.member.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/photographer")
public class PhotoAdminController {

    private final PhotographerService photographerService;
    private final ResponseService responseService;

    @DeleteMapping("/{id}")
    public SingleResult<PhotographerResponseDto> delete (@PathVariable Long id){
        return responseService.getSingleResult(photographerService.delete(id));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PhotographerResponseDto>> findAll(PhotographerSearchCondition condition, Pageable pageable){
        Page<PhotographerResponseDto> photographerResponseDtos = photographerService.findByQuerydsl(condition, pageable);
        return ResponseEntity.ok(photographerResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotographerResponseDto> findOne(@PathVariable("id") Long id){
        PhotographerResponseDto photographerResponseDto = photographerService.getOne(id);
        return ResponseEntity.ok(photographerResponseDto);
    }

}
