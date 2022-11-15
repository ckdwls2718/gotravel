package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.LocationSearchDto;
import com.scnu.gotravel.dto.PhotographerSearchDto;
import com.scnu.gotravel.service.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/search")
public class SearchController {

    private final Search search;

    @GetMapping("/board")
    public ResponseEntity<LocationSearchDto> boardLocationSearch(){
        LocationSearchDto locationSearchDto = search.showBoardLocationAndHashTag();
        return ResponseEntity.ok().body(locationSearchDto);
    }

    @GetMapping("/photographer")
    public ResponseEntity<PhotographerSearchDto> photographerLocationSearch(){
        PhotographerSearchDto photographerSearchDto = search.showPhotographerLocationAndHashTag();
        return ResponseEntity.ok().body(photographerSearchDto);
    }
}
