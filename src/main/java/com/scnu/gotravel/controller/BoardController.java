package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.dto.HashTagRecommendDto;
import com.scnu.gotravel.dto.ResponseMapDto;
import com.scnu.gotravel.dto.board.RequestBoardDto;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.service.BoardService;
import com.scnu.gotravel.service.HashTagService;
import com.scnu.gotravel.service.RecommendService;
import com.scnu.gotravel.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final HashTagService hashTagService;
    private final RecommendService recommendService;
    private final S3Uploader s3Uploader;
    private final String dirName = "boardImg";

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBoardDto> findOne(@PathVariable("id") Long id) {
        ResponseBoardDto result = boardService.findOne(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("")
    public ResponseEntity<Page<ResponseBoardDto>> searchByQuerydsl(BoardSearchCondition boardSearchCondition, Pageable pageable) {
        Page<ResponseBoardDto> resultList = boardService.findByQuerydsl(boardSearchCondition, pageable);
        return ResponseEntity.ok().body(resultList);
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestPart("images") List<MultipartFile> multipartFiles,
                                  @RequestPart("dto") RequestBoardDto requestBoardDto) throws FileNotFoundException {

        log.info("file : " + multipartFiles);
        log.info("board : " + requestBoardDto);
        // 파일 확장자 검사

        if (requestBoardDto.getLocationNum() < 18){
            throw new RuntimeException("지역을 다시 선택해주세요");
        }

        boolean checkResult = checkExt(multipartFiles);
        if(!checkResult) throw new FileNotFoundException();
        List<String> failUrlList = null;

        try{
            List<String> urlList = s3Uploader.uploadFiles(multipartFiles, dirName);
            failUrlList = urlList.stream().collect(Collectors.toList());

            boardService.save(requestBoardDto, urlList);

            return ResponseEntity.ok().body(urlList);

        }catch (Exception e){
            for (String url : failUrlList) {
                String[] split = url.split("/");
                String filename = dirName +"/" + split[split.length - 1];
                s3Uploader.deleteS3(filename);
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> recommend(@PathVariable("id")Long boardId){
        String result = recommendService.addRecommend(boardId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    public HttpStatus modify(@RequestBody ResponseBoardDto responseBoardDto){

        log.info("board : " + responseBoardDto);

        boardService.modify(responseBoardDto);

        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") Long id){
        log.info("id : " + id);
        boardService.delete(id);
        return HttpStatus.OK;
    }

    @GetMapping("/rank")
    public ResponseEntity<List<ResponseBoardDto>> findAllByRank() {
        List<ResponseBoardDto> findBoardList = boardService.findByRank();
        return ResponseEntity.ok().body(findBoardList);}

    @GetMapping("/hashtag")
    public ResponseEntity<List<HashTagRecommendDto>> findByRecommendHashTag(int count){
        List<HashTagRecommendDto> hashTagRecommendDtoList = boardService.findByHashTag(hashTagService.recommendHashTags(count));

        return ResponseEntity.ok().body(hashTagRecommendDtoList);
    }


    @GetMapping("/map")
    public ResponseEntity<List<ResponseMapDto>> countByLocation() {
        List<ResponseMapDto> resultList = boardService.countByLocation();

        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/map/{locationNum}")
    public ResponseEntity<List<ResponseBoardDto>> findByLocationNum(@PathVariable("locationNum") Long locationNum){
        log.info("locationNum : " + locationNum);
        List<ResponseBoardDto> resultList = boardService.findByLocationNum(locationNum);
        return ResponseEntity.ok().body(resultList);
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

//    @GetMapping("/list")
//    public  List<ResponseBoardDto> boardList(){
//        List<ResponseBoardDto> boardList = boardService.findAll();
//        return boardList;
//    }

// 페이징처리
//    @GetMapping("/list/{page}")
//    public Object getBoardList(@PathVariable("page")@Min(0) Integer page)throws Exception{
//        return boardService.findAll(page);
//    }
}
