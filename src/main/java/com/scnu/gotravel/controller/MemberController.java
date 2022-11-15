package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.MemberDTO;
import com.scnu.gotravel.dto.TokenResponseDto;
import com.scnu.gotravel.dto.member.*;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.service.MemberService;
import com.scnu.gotravel.service.ResponseService;
import com.scnu.gotravel.service.member.MultipleResult;
import com.scnu.gotravel.service.member.SingleResult;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;

    // 회원가입
    @PostMapping("/register")
    public SingleResult<MemberRegisterResponseDto> register(@RequestBody MemberRegisterRequestDto requestDto) {
        log.info("dto : " + requestDto);
        MemberRegisterResponseDto responseDto = memberService.register(requestDto);
        return responseService.getSingleResult(responseDto);
    }
    // 로그인
    @PostMapping("/login")
    public SingleResult<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        log.info("requestDto : " +requestDto);
        MemberLoginResponseDto responseDto = memberService.login(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public SingleResult<TokenResponseDto> reIssue(@RequestBody TokenRequestDto tokenRequestDto) {
        log.info("tokenRequestDto : " + tokenRequestDto);
        TokenResponseDto responseDto = memberService.reIssue(tokenRequestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public HttpStatus delete(){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        if(memberService.delete(memberEmail)) return HttpStatus.OK;
        else return HttpStatus.BAD_REQUEST;
    }

}
