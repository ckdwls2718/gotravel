package com.scnu.gotravel.service;

import com.scnu.gotravel.config.exception.MemberEmailAlreadyExistsException;
import com.scnu.gotravel.config.exception.MemberNotFoundException;
import com.scnu.gotravel.dto.MemberDTO;
import com.scnu.gotravel.dto.MemberSearchCondition;
import com.scnu.gotravel.dto.member.MemberRegisterRequestDto;
import com.scnu.gotravel.dto.member.MemberRegisterResponseDto;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.entity.Role;
import com.scnu.gotravel.repository.BoardRepository;
import com.scnu.gotravel.repository.LetterRepository;
import com.scnu.gotravel.repository.MemberAdminRepositoryImpl;
import com.scnu.gotravel.repository.MemberRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAdminService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberAdminRepositoryImpl memberAdminRepositoryImpl;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final PhotographerService photographerService;
    private final PhotographerRepository photographerRepository;
    private final LetterRepository letterRepository;
    private final LetterService letterService;

    @Transactional // 회원가입(이메일, 닉네임, 이름, 비밀번호 작성)
    public MemberRegisterResponseDto register(MemberRegisterRequestDto requestDto) {

        validateDuplicated(requestDto.getEmail(), requestDto.getNickname());

        Member member = memberRepository.save(
                Member.builder()
                        .email(requestDto.getEmail())
                        .nickName(requestDto.getNickname())
                        .name(requestDto.getName())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .role(Collections.singletonList(Role.ROLE_ADMIN))
                        .build());
        return new MemberRegisterResponseDto(member.getId(), member.getEmail(), member.getName(), member.getNickName());
    }

    public Page<MemberDTO> findAll(MemberSearchCondition memberSearchCondition, Pageable pageable){
        List<MemberDTO> resultList = new ArrayList<>();

        Page<Member> result = memberAdminRepositoryImpl.search(memberSearchCondition, pageable);
        result.forEach(member -> resultList.add(entityToDto(member)));

        return new PageImpl<>(resultList,pageable,result.getTotalElements());
    }

    public MemberDTO findOne(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다"));
        MemberDTO memberDTO = entityToDto(member);
        return memberDTO;
    }

    @Transactional
    public void delete(Long id){
        memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다"));

        Optional<List<Long>> receiveLetterId = letterRepository.findByReceiveMemberId(id);

        if(receiveLetterId.isPresent()){
            List<Long>receiveLetterIdList = receiveLetterId.get();
            log.info("receiveLetterIdList = " + receiveLetterIdList);
            for(Long letterId : receiveLetterIdList) letterService.delete(letterId);
        }

        Optional<List<Long>> sendLetterId = letterRepository.findBySendMemberId(id);

        if(sendLetterId.isPresent()){
            List<Long>sendLetterIdList = sendLetterId.get();
            log.info("sendLetterIdList = " + sendLetterIdList);
            for(Long letterId : sendLetterIdList) letterService.delete(letterId);
        }

        Optional<List<Long>> boardId = boardRepository.findByMemberId(id);
        if(boardId.isPresent()){
            List<Long> boardIdList = boardId.get();
            log.info("boardIdList = " + boardIdList);
            for(Long bId : boardIdList) boardService.delete(bId);
        }

        Optional<List<Long>> photographerId = photographerRepository.findByMemberId(id);
        if(photographerId.isPresent()){
            List<Long> photographerIdList = photographerId.get();
            log.info("photographerIdList = " + photographerIdList);
            for(Long pId : photographerIdList) photographerService.delete(pId);
        }
        memberRepository.deleteById(id);
    }

    private MemberDTO entityToDto(Member member){
        MemberDTO memberDTO = MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickName())
                .build();
        return memberDTO;
    }
    // 닉네임과 이메일 중복체크
    private void validateDuplicated(String email, String nickname) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new MemberEmailAlreadyExistsException();
        if (memberRepository.findByNickName(nickname).isPresent())
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        // 여기 블로그 필자는 예외를 한 파일에서 다 관리하는 듯함...
    }
}
