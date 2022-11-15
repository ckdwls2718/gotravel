package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.board.NoticeBoardRequestDto;
import com.scnu.gotravel.dto.board.NoticeBoardResponseDto;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.entity.NoticeBoard;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.MemberRepository;
import com.scnu.gotravel.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeBoardService {

    private final MemberRepository memberRepository;
    private final NoticeBoardRepository noticeBoardRepository;

    @Transactional // 공지사항 작성하기
    public Long NoticeSave(NoticeBoardRequestDto noticeBoardRequestDto){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        NoticeBoard noticeBoard = NoticeBoard.builder()
                .content(noticeBoardRequestDto.getContent())
                .title(noticeBoardRequestDto.getTitle())
                .member(member)
                .build();
        noticeBoardRepository.save(noticeBoard);
        return noticeBoard.getId();
    }

    // 공지사항 하나 가져오기
    public NoticeBoardResponseDto findNoticeOne(Long id) {

        NoticeBoard noticeBoard = noticeBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        NoticeBoardResponseDto noticeBoardResponseDto = find(noticeBoard);

        return noticeBoardResponseDto;
    }

    // 공지사항 다 가져오기
    public List<NoticeBoardResponseDto> findAllNotice(){
        List<NoticeBoard> resultList = noticeBoardRepository.findAll();
        List<NoticeBoardResponseDto> NBR = new ArrayList<>();
        resultList.forEach(entity -> {
            NoticeBoardResponseDto noticeBoardResponseDto = find(entity);
            NBR.add(noticeBoardResponseDto);
        });

        return NBR;
    }
    // 공지사항 삭제
    @Transactional
    public void NoticeDelete(Long id){
        noticeBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        noticeBoardRepository.deleteById(id);
    }

    private NoticeBoardResponseDto find(NoticeBoard noticeBoard){

        NoticeBoardResponseDto noticeBoardResponseDto = NoticeBoardResponseDto.builder()
                .id(noticeBoard.getId())
                .content(noticeBoard.getContent())
                .title(noticeBoard.getTitle())
                .memberId(noticeBoard.getMember().getNickName())
                .createDate(noticeBoard.getCreateDate()).build();
        return noticeBoardResponseDto;
    }
}
