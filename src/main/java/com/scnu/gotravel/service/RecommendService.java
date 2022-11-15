package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.entity.*;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.BoardRepository;
import com.scnu.gotravel.repository.MemberRepository;
import com.scnu.gotravel.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String addRecommend(Long boardId) {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        Optional<Recommend> findRecommend = duplicateCheck(boardId, loginMemberEmail);

        if (findRecommend.isEmpty() == true) {
            Board findBoard = boardRepository.findById(boardId).get();
            Member findMember = memberRepository.findByEmail(loginMemberEmail).get();

            Recommend recommend = Recommend.builder()
                    .board(findBoard)
                    .member(findMember)
                    .build();

            recommendRepository.save(recommend);

            findBoard.plusRecommend();

            return "like";

        } else {
            Recommend recommend = findRecommend.orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
            Board findBoard = recommend.getBoard();

            recommendRepository.deleteById(recommend.getId());

            findBoard.minusRecommend();

            return "dislike";

        }
    }

    public List<ResponseBoardDto> findAll() {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();
        List<Board> boards = recommendRepository.findByMemberEmail(loginMemberEmail);


        List<ResponseBoardDto> boardDtoList = new ArrayList<>();

        for (Board findBoard : boards) {

            List<String> hashTag = new ArrayList<>();
            List<String> imageList = new ArrayList<>();

            for (Board_HashTag board_hashTag : findBoard.getBoard_hashTags()) {
                hashTag.add(board_hashTag.getHashTag().getHashTagName());
            }

            for (Image image : findBoard.getImageList()) {
                imageList.add(image.getImageURL());
            }

            boardDtoList.add(ResponseBoardDto.builder()
                    .id(findBoard.getId())
                    .title(findBoard.getTitle())
                    .content(findBoard.getContent())
                    .memberId(findBoard.getMember().getNickName())
                    .board_hashTags(hashTag)
                    .location(findBoard.getLocation().getName())
                    .imageList(imageList)
                    .createDate(findBoard.getCreateDate())
                    .recommendCount(findBoard.getRecommendCount())
                    .build());
        }

        return boardDtoList;
    }

    @Transactional
    public void deleteRecommend(Long recommendId) {
        Board findBoard = boardRepository.findByRecommendId(recommendId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 게시물입니다."));
        recommendRepository.deleteById(recommendId);
        findBoard.minusRecommend();
    }

    public Optional<Recommend> duplicateCheck(Long boardId, String email) {
        Optional<Recommend> recommend = recommendRepository.findByBoardAndMember(boardId, email);
        return recommend;
    }
}
