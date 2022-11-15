package com.scnu.gotravel.controller;


import com.scnu.gotravel.config.exception.PhotographerExistException;
import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.dto.photographer.PhotographerRequestDto;
import com.scnu.gotravel.dto.photographer.PhotographerResponseDto;
import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;
import com.scnu.gotravel.repository.Photographer.PhotographerImageRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerRepository;
import com.scnu.gotravel.service.PhotographerService;
import com.scnu.gotravel.service.ResponseService;
import com.scnu.gotravel.service.S3Uploader;
import com.scnu.gotravel.service.member.MultipleResult;
import com.scnu.gotravel.service.member.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/photographer")
public class PhotographerController {
    private final PhotographerService photographerService;
    private final S3Uploader s3Uploader;
    private final ResponseService responseService;
    private static final String dirName = "photographerImg";
    private final PhotographerImageRepository photographerImageRepository;


    // 사진작가 등록 -> 수정사항 : 예외처리를 예외처리 파일에서 할 것
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public SingleResult<String> save(@RequestPart("images") List<MultipartFile> multipartFiles,
                                     @RequestPart("dto") PhotographerRequestDto photographerRequestDto) {
        log.info("file : " + multipartFiles);
        log.info("dto : " + photographerRequestDto);
        boolean checkResult = checkExt(multipartFiles);
        if(!checkResult) throw new PhotographerExistException();

        List<String> failUrlList = null;
        try {
            List<String> urlList = s3Uploader.uploadFiles(multipartFiles, dirName);
            failUrlList = urlList.stream().collect(Collectors.toList());
            photographerService.save(photographerRequestDto, urlList);
            return responseService.getSingleResult("등록완료");
        } catch (Exception e) {
            for (String url : failUrlList) {
                String[] split = url.split("/");
                String filename = dirName + "/" + split[split.length - 1];
            }
            throw new PhotographerExistException();
        }
    }

    // 사진작가 삭제
    @DeleteMapping("/{id}")
    public SingleResult<PhotographerResponseDto> delete(@PathVariable Long id) {
        log.info("id : " + id);
        return responseService.getSingleResult(photographerService.delete(id));
    }

    @GetMapping("/{id}")
    public SingleResult<PhotographerResponseDto> getOne(@PathVariable("id") Long id) {
        log.info("id : " + id);
        return responseService.getSingleResult(photographerService.getOne(id));
    }

//    @GetMapping("/list") // 기존에 만들었던 MulipleResult를 이용한 방식 -> 2번쨰로
//    public MultipleResult<PhotographerResponseDto> getAll(){
//            List<PhotographerResponseDto> photographerList = photographerService.findAll();
//        return responseService.getMultipleResult(photographerList);
//    }

    @GetMapping("/list") // querydsl을 이용한 방식 -> 가장 빨랐음
   public ResponseEntity<Page<PhotographerResponseDto>> findAll(PhotographerSearchCondition condition, Pageable pageable){
        Page<PhotographerResponseDto> photographerResponseDtos = photographerService.findByQuerydsl(condition, pageable);
        return ResponseEntity.ok(photographerResponseDtos);
    }

//    @GetMapping("/list") // 기존의 방식 -> postman으로 확인했을 때, 가장 느렸음
//    public List<PhotographerResponseDto> getAll(){
//        List<PhotographerResponseDto> photographerList = photographerService.findAll();
//        return photographerList;
//    }

    @GetMapping("")
    public ResponseEntity<Page<PhotographerResponseDto>> searchByQuerydsl(PhotographerSearchCondition photographerSearchCondition, Pageable pageable){
        log.info("photographerSearchCondition : " + photographerSearchCondition);
        Page<PhotographerResponseDto> resultList = photographerService.findByQuerydsl(photographerSearchCondition,pageable);
        return ResponseEntity.ok().body(resultList);
    }

    @PutMapping("")
    public HttpStatus changePhotographer(@RequestBody PhotographerResponseDto photographerResponseDto){
        photographerService.photographerModify(photographerResponseDto);
        return HttpStatus.OK;
    }

    // 파일 확장자 검사 메서드
    private boolean checkExt(List<MultipartFile> multipartFiles){
        List<String> ext = new ArrayList<>();
        boolean result = true;

        multipartFiles.forEach( multipartFile -> {
            int pos = multipartFile.getOriginalFilename().lastIndexOf(".");
            ext.add(multipartFile.getOriginalFilename().substring(pos + 1));
            log.info(multipartFile.getOriginalFilename().substring(pos + 1));
        });

        for(String extOne : ext){
            String smallExtOne = extOne.toLowerCase(Locale.ROOT);
            log.info(smallExtOne);
            switch (smallExtOne){
                case "jpg" : break;
                case "png" : break;
                case "jpeg" : break;
                default: result = false; break;
            }
        }
        return result;
    }
}