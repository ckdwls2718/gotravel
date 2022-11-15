package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.dto.HashTagRecommendDto;
import com.scnu.gotravel.dto.ResponseMapDto;
import com.scnu.gotravel.dto.board.RequestBoardDto;
import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.entity.*;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final Board_HashTagRepository board_hashTagRepository;
    private final ImageRepository imageRepository;
    private final RecommendService recommendService;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    @Transactional // 일반 게시글 저장
    public Long save(RequestBoardDto requestBoardDto, List<String> urlList){

        Board board = dtoToEntity(requestBoardDto);
        Board saveBoard = boardRepository.save(board);
        log.info("board : " + saveBoard);
        log.info("dto : " + requestBoardDto);

        requestBoardDto.getHashTag().forEach(hashTagNum -> {

            HashTag hashTag = HashTag.builder()
                    .id(hashTagNum)
                    .build();

            Board_HashTag board_hashTag = Board_HashTag.builder()
                    .hashTag(hashTag)
                    .board(board)
                    .build();
            board_hashTagRepository.save(board_hashTag);
        });

        urlList.forEach(url -> {
            Image image = Image.builder().imageURL(url).board(saveBoard).build();
            imageRepository.save(image);
        });

        return saveBoard.getId();
    }

    //한번 거르고 한번 더
    public Page<ResponseBoardDto> findByQuerydsl(BoardSearchCondition boardSearchCondition, Pageable pageable) {

        List<ResponseBoardDto> boardDtoList = new ArrayList<>();

        Page<Board> boardList = boardRepository.search(boardSearchCondition, pageable);

        if(!boardSearchCondition.getHashTags().isEmpty()){
            for (Board board : boardList) {
                ResponseBoardDto boardDto = entityToDto(board);
                if(hashTagCheck(boardDto.getBoard_hashTags(), boardSearchCondition.getHashTags())){
                    boardDtoList.add(boardDto);
                }
            }
        } else{
            boardList.forEach(e -> boardDtoList.add(entityToDto(e)));
        }

        return new PageImpl<>(boardDtoList,pageable, boardList.getTotalElements());
    }

    public List<ResponseBoardDto> findByRank() {

        List<ResponseBoardDto> boardDtoList = new ArrayList<>();

        List<Board> findBoard = boardRepository.findByRank()
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        for (Board board : findBoard) {
            boardDtoList.add(entityToDto(board));
        }

        return boardDtoList;
    }

    public List<HashTagRecommendDto> findByHashTag(List<String> hashTags){

        List<HashTagRecommendDto> result = new ArrayList<>();

        List<Board> boardList = boardRepository.findByHashTags(hashTags)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 없습니다."));

        for(String hashTag : hashTags){

            List<ResponseBoardDto> boardDtoList = new ArrayList<>();

            for (Board board : boardList){
                ResponseBoardDto boardDto = entityToDto(board);
                if (boardDto.getBoard_hashTags().contains(hashTag)){
                    boardDtoList.add(boardDto);
                }

                if(boardDtoList.size()>=4)
                    break;
            }

            result.add(HashTagRecommendDto.builder()
                    .hashTagName(hashTag)
                    .boardDtoList(boardDtoList)
                    .build());
        }

        return result;
    }


    @Transactional(readOnly = true)
    public List<ResponseBoardDto> findByLocationNum(Long locationNum){
        List<ResponseBoardDto> resultBoardList = new ArrayList<>();

        List<Board> boardList = boardRepository.findByLocationNum(locationNum).orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        boardList.forEach(board -> {
            ResponseBoardDto responseBoardDto = entityToDto(board);
            resultBoardList.add(responseBoardDto);
        });
        return resultBoardList;
    }

    public ResponseBoardDto findOne(Long id){
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        ResponseBoardDto responseBoardDto = entityToDto(board);
        return responseBoardDto;
    }

    public List<ResponseBoardDto> findByMemberId(String email) {
        List<ResponseBoardDto> boardDtoList = new ArrayList<>();

        List<Board> boardList = boardRepository.findByMemberNickname(email).orElseThrow(() -> new IllegalArgumentException("게시물이 없습니다"));

        boardList.forEach(e -> boardDtoList.add(entityToDto(e)));

        return boardDtoList;
    }

    public List<ResponseMapDto> countByLocation(){
        List<ResponseMapDto> requestMapDtoList = boardRepository.countByLocation()
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
        return requestMapDtoList;
    }

    @Transactional
    public void modify(ResponseBoardDto responseBoardDto){
        Board board = boardRepository.findById(responseBoardDto.getId()).orElseThrow(() -> new IllegalArgumentException());
        board.changeTitleAndContent(responseBoardDto.getTitle(), responseBoardDto.getContent());
    }

    @Transactional
    public void delete(Long id){
        boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        List<Long> recommendList= recommendRepository.findByBoardId(id).orElseThrow(() -> new IllegalArgumentException());

        recommendList.forEach(recommendId -> {
            recommendService.deleteRecommend(recommendId);
        });

        imageRepository.deleteByBoardId(id);
        board_hashTagRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }

    private Board dtoToEntity(RequestBoardDto requestBoardDto){

        Location location = Location.builder().id(requestBoardDto.getLocationNum()).build();
        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();
        Member loginMember = memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Board board = Board.builder()
                .content(requestBoardDto.getContent())
                .location(location)
                .member(loginMember)
                .title(requestBoardDto.getTitle())
                .build();

        return board;
    }

    private ResponseBoardDto entityToDto(Board entity){

        List<String> hashTagName = entity.getBoard_hashTags().stream()
                .map(e -> e.getHashTag().getHashTagName())
                .collect(Collectors.toList());

        List<String> urlList = entity.getImageList().stream()
                .map(e -> e.getImageURL())
                .collect(Collectors.toList());

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();
        int likeCheck = (recommendService.duplicateCheck(entity.getId(), loginMemberEmail).isEmpty()) ? 0 : 1;

        String location = entity.getLocation().getParent().getName()+" "+entity.getLocation().getName();

        ResponseBoardDto responseBoardDto = ResponseBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .memberId(entity.getMember().getEmail())
                .board_hashTags(hashTagName)
                .location(location)
                .imageList(urlList)
                .recommendCount(entity.getRecommendCount())
                .likeCheck(likeCheck)
                .createDate(entity.getCreateDate())
                .nickName(entity.getMember().getNickName())
                .build();

        return responseBoardDto;
    }

    private Boolean hashTagCheck(List<String> boardHashTags, List<String> conditionHashTags){
        return boardHashTags.containsAll(conditionHashTags);
    }
}
