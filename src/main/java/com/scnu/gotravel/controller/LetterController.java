package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.LetterDto;
import com.scnu.gotravel.dto.LetterSearchCondition;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.service.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/letter")
public class LetterController {

    private final LetterService letterService;

    // 모든 메시지 출력
    @GetMapping("")
    public ResponseEntity<List<LetterDto>> findAll(@Param("flag") int flag) {
        log.info("flag : " + flag);
        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();
        log.info(loginMemberEmail + "find Letters");
        List<LetterDto> result = letterService.searchByQuerydsl(loginMemberEmail, flag);
        return ResponseEntity.ok().body(result);
    }

    // 보낸 메시지 출력
    @GetMapping("/send")
    public ResponseEntity<List<LetterDto>> findAllBySendMember() {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        List<LetterDto> letterDtoList = letterService.sendLetterList(loginMemberEmail);

        return ResponseEntity.ok().body(letterDtoList);
    }

    // 받은 메시지 출력
    @GetMapping("/receive")
    public ResponseEntity<List<LetterDto>> findAllByReceiveMember() {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        List<LetterDto> letterDtoList = letterService.receiveLetterList(loginMemberEmail);

        return ResponseEntity.ok().body(letterDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LetterDto> findOne(@PathVariable("id") Long letterId) {

        LetterDto letterDto = letterService.findOne(letterId);

        log.info("findOne letter :" +letterDto);

        return ResponseEntity.ok().body(letterDto);
    }

    @PostMapping(value = "", produces = "application/json; charset=utf8")
    public ResponseEntity<String> save(@RequestBody LetterDto letterDto) {

        log.info("save letter : " + letterDto);

        try {
            letterService.save(letterDto);
            return ResponseEntity.ok().body(letterDto.getReceiveMember() + "님께 전송하였습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<String> modify(@RequestBody LetterDto letterDto) {
        log.info("modify letter" + letterDto);
        try {
            letterService.modify(letterDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("letter delete id: "+id);
        letterService.delete(id);
    }

}
