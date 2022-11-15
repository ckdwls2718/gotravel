package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.LetterDto;
import com.scnu.gotravel.dto.LetterSearchCondition;
import com.scnu.gotravel.entity.Letter;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.LetterRepository;
import com.scnu.gotravel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(LetterDto letterDto) {

        try {
            Letter letter = dtoToEntity(letterDto);
            letterRepository.save(letter);
            log.info("save Letter" + letter.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LetterDto findOne(Long id) {
        Letter letter = letterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메세지입니다."));

        log.info("findOne Letter" + letter.getTitle());

        return entityToDTO(letter);
    }

    // memberId를 이용해 모든 메시지 출력
    public List<LetterDto> findAll(String sendMember) {

        List<Letter> all = letterRepository.findByAllByMember(sendMember).orElseThrow(() -> new IllegalArgumentException("메시지를 불러올 수 없습니다."));
        List<LetterDto> letterDtoList = new ArrayList<>();

        all.forEach(e ->letterDtoList.add(entityToDTO(e)));

        log.info("findAll Letter");
        return letterDtoList;
    }

    //sendMemberId를 이용해 동적쿼리 출력
    public List<LetterDto> searchByQuerydsl(String email, int flag) {
        List<LetterDto> letterDtoList = new ArrayList<>();

        List<Letter> search = letterRepository.search(email,flag);

        search.forEach(e -> letterDtoList.add(entityToDTO(e)));

        return letterDtoList;
    }

    @Transactional
    public void modify(LetterDto letterDto) {
        Letter letter = letterRepository.findById(letterDto.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));

        letter.changeTitleAndContent(letterDto);

    }

    @Transactional
    public void delete(Long id) {
        letterRepository.deleteById(id);
        log.info("delete Letter :" + id);
    }

    // 보낸 메세지함
    public List<LetterDto> sendLetterList(String email) {

        List<LetterDto> letterDtoList = new ArrayList<>();

        List<Letter> letterList = letterRepository.findByAllBySendMemberEmail(email).orElseThrow(() -> new IllegalArgumentException("메세지를 불러올 수 없습니다"));

        letterList.forEach(e -> letterDtoList.add(entityToDTO(e)));

        return letterDtoList;
    }

    // 받은 메세지함
    public List<LetterDto> receiveLetterList(String email) {

        List<LetterDto> letterDtoList = new ArrayList<>();

        List<Letter> letterList = letterRepository.findByAllByReceiveMemberEmail(email).orElseThrow(() -> new IllegalArgumentException("메세지를 불러올 수 없습니다"));

        letterList.forEach(e -> letterDtoList.add(entityToDTO(e)));

        return letterDtoList;
    }

    public LetterDto entityToDTO(Letter letter) {
        return LetterDto.builder()
                .id(letter.getId())
                .sendMember(letter.getSendMember().getEmail())
                .receiveMember(letter.getReceiveMember().getEmail())
                .title(letter.getTitle())
                .content(letter.getContent())
                .build();
    }


    public Letter dtoToEntity(LetterDto letterDto) {
        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        Member sendMember = memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다."));

        Member receiveMember = memberRepository.findByEmail(letterDto.getReceiveMember())
                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다."));

        return Letter.builder()
                .id(letterDto.getId())
                .sendMember(sendMember)
                .receiveMember(receiveMember)
                .title(letterDto.getTitle())
                .content(letterDto.getContent())
                .build();
    }
}

