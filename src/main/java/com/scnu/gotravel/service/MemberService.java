package com.scnu.gotravel.service;


import com.scnu.gotravel.auth.AccessToken;
import com.scnu.gotravel.auth.profile.ProfileDto;
import com.scnu.gotravel.config.exception.InvalidRefreshTokenException;
import com.scnu.gotravel.config.exception.LoginFailureException;
import com.scnu.gotravel.config.exception.MemberEmailAlreadyExistsException;
import com.scnu.gotravel.config.exception.MemberNotFoundException;

import com.scnu.gotravel.dto.MemberDTO;
import com.scnu.gotravel.dto.TokenResponseDto;
import com.scnu.gotravel.dto.member.*;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.entity.Role;
import com.scnu.gotravel.jwt.JwtTokenProvider;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.BoardRepository;
import com.scnu.gotravel.repository.LetterRepository;
import com.scnu.gotravel.repository.MemberRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final PhotographerService photographerService;
    private final PhotographerRepository photographerRepository;
    private final LetterRepository letterRepository;
    private final LetterService letterService;
//    private final ProviderService providerService;


    @Transactional // ????????????(?????????, ?????????, ??????, ???????????? ??????)
    public MemberRegisterResponseDto register(MemberRegisterRequestDto requestDto) {

        validateDuplicated(requestDto.getEmail(), requestDto.getNickname());

        Member member = memberRepository.save(
                Member.builder()
                        .email(requestDto.getEmail())
                        .nickName(requestDto.getNickname())
                        .name(requestDto.getName())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .role(Collections.singletonList(Role.ROLE_USER))
                        .build());
        return new MemberRegisterResponseDto(member.getId(), member.getEmail(), member.getName(), member.getNickName());
    }

    // ???????????? ????????? ????????????
    private void validateDuplicated(String email, String nickname) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new MemberEmailAlreadyExistsException();
        if (memberRepository.findByNickName(nickname).isPresent())
            throw new IllegalStateException("?????? ???????????? ??????????????????.");
        // ?????? ????????? ????????? ????????? ??? ???????????? ??? ???????????? ??????...
    }

    @Transactional // ????????? (???????????? ????????????)
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword()))
            throw new LoginFailureException();
        member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(requestDto.getEmail()), member.getRefreshToken());
        // ????????? ????????? pk?????? email??? ?????? ???????????? ????????? ????????? -> ?????? ?????? ??????????????? ????????? ????????? ??????.
    }

//    @Transactional
//    public MemberLoginResponseDto loginMemberByProvider(String code, String provider){
//        AccessToken accessToken = providerService.getAccessToken(code, provider);
//        ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), provider);
//
//
//        Optional<Member> findMember = memberRepository.findByEmailAndProvider(profile.getEmail(), provider);
//        if (findMember.isPresent()) {
//            Member member = findMember.get();
//            member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
//            return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(findMember.get().getEmail()), member.getRefreshToken());
//        } else {
//            Member saveMember = saveMember(profile, provider);
//            saveMember.updateRefreshToken(jwtTokenProvider.createRefreshToken());
//            return new MemberLoginResponseDto(saveMember.getId(), jwtTokenProvider.createToken(saveMember.getEmail()), saveMember.getRefreshToken());
//        }
//
//    }

    private Member saveMember(ProfileDto profile, String provider) {
        Member member = Member.builder()
                .email(profile.getEmail())
                .password(null)
                .provider(provider)
                .build();
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }


    @Transactional // ?????? ????????? (????????? access ????????? Refresh ??????)
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        if (!jwtTokenProvider.validateTokenExpiration(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();
        Member member = findMemberByToken(requestDto);
        if (!member.getRefreshToken().equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();
        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(); // ???????????? ????????? ?????? ?????????? ????????? ??????????????
        member.updateRefreshToken(refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }


    public Member findMemberByToken(TokenRequestDto requestDto) {
        Authentication auth = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
    }

    public MemberDTO getMemberInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        MemberDTO memberDTO = entityToDto(member);
        return memberDTO;
    }

    public MemberDTO getMyInfo(){
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("???????????? ?????? ??? ????????????."));
        return entityToDto(member);
    }

    @Transactional
    public boolean delete(String memberEmail){
        Member findMember = memberRepository.findByEmail(memberEmail).orElseThrow(() -> new RuntimeException("???????????? ?????? ??? ????????????."));

        Long memberId = findMember.getId();

        Optional<List<Long>> receiveLetterId = letterRepository.findByReceiveMemberId(memberId);

        if(receiveLetterId.isPresent()){
            List<Long>receiveLetterIdList = receiveLetterId.get();
            log.info("receiveLetterIdList = " + receiveLetterIdList);
            for(Long letterId : receiveLetterIdList) letterService.delete(letterId);
        }

        Optional<List<Long>> sendLetterId = letterRepository.findBySendMemberId(memberId);

        if(sendLetterId.isPresent()){
            List<Long>sendLetterIdList = sendLetterId.get();
            log.info("sendLetterIdList = " + sendLetterIdList);
            for(Long letterId : sendLetterIdList) letterService.delete(letterId);
        }

        Optional<List<Long>> boardId = boardRepository.findByMemberId(memberId);
        if(boardId.isPresent()){
            List<Long> boardIdList = boardId.get();
            log.info("boardIdList = " + boardIdList);
            for(Long bId : boardIdList) boardService.delete(bId);
        }

        Optional<List<Long>> photographerId = photographerRepository.findByMemberId(memberId);
        if(photographerId.isPresent()){
            List<Long> photographerIdList = photographerId.get();
            log.info("photographerIdList = " + photographerIdList);
            for(Long pId : photographerIdList) photographerService.delete(pId);
        }
        memberRepository.deleteById(memberId);

        return true;
    }

    private MemberDTO entityToDto(Member member){

        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickName())
                .email(member.getEmail())
                .build();
    }

}



