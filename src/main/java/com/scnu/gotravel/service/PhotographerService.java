package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.photographer.PhotographerRequestDto;
import com.scnu.gotravel.dto.photographer.PhotographerResponseDto;
import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;
import com.scnu.gotravel.entity.*;
import com.scnu.gotravel.jwt.SecurityUtil;
import com.scnu.gotravel.repository.*;
import com.scnu.gotravel.repository.Photographer.PhotographerHashTagRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerImageRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotographerService {

    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;
    private final PhotographerImageRepository photographerImageRepository;
    private final PhotographerHashTagRepository photographerHashTagRepository;

    @Transactional    // 사진작가 등록
    public Long save(PhotographerRequestDto photographerRequestDto, List<String> urlList) {

        log.info("photographerRequestDto : " + photographerRequestDto);
        log.info("urlList : " + urlList);

        Photographer photographer = dtoToEntity(photographerRequestDto);
        Photographer savePhotographer = photographerRepository.save(photographer);

        photographerRequestDto.getHashtag().forEach(hashtagNum->{
            PhotographerHashTagList photographerHashTagList = PhotographerHashTagList.builder()
                    .id(hashtagNum)
                    .build();

            PhotographerHashTag photographerHashTag = PhotographerHashTag.builder()
                    .photographerHashTagList(photographerHashTagList)
                    .photographer(savePhotographer).build();
            photographerHashTagRepository.save(photographerHashTag);
        });
        urlList.forEach(url -> {
            PhotographerImage photographerImage = PhotographerImage.builder()
                    .photographer(savePhotographer)
                    .imageURL(url).build();
            photographerImageRepository.save(photographerImage);
        });
        log.info("savePhotographer"+savePhotographer);
    return savePhotographer.getId();
    }

    @Transactional // 사진작가 등록 해제
    public PhotographerResponseDto delete(Long id) {
        Photographer photographer = photographerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                ("해당 사진작가 정보는 없습니다. id =" + id)));
        PhotographerResponseDto photographerResponseDto = entityToDto(photographer);
        photographerRepository.delete(photographer);
        return photographerResponseDto;
    }


    public PhotographerResponseDto getOne(Long id) {
        Optional<Photographer> findPhotographer = photographerRepository.findById(id);
        Photographer photographer = findPhotographer.get();
        PhotographerResponseDto photographerResponseDto = entityToDto(photographer);
        return photographerResponseDto;

    }

    // 쿼리dsl로도 전체조회 가능
    public Page<PhotographerResponseDto> findByQuerydsl(PhotographerSearchCondition photographerSearchCondition, Pageable pageable){
        List<PhotographerResponseDto> result = new ArrayList<>();
        Page<Photographer> photographerList = photographerRepository.search(photographerSearchCondition, pageable);

        if(!photographerSearchCondition.getHashtag().isEmpty()){
            for(Photographer photographer : photographerList){
                PhotographerResponseDto responseDto = entityToDto(photographer);
                if(hashTagCheck(responseDto.getHashtag(), photographerSearchCondition.getHashtag())){
                    result.add(responseDto);
                }
            }
        }else {
            photographerList.forEach(e->result.add(entityToDto(e)));
        }

        return new PageImpl<>(result, pageable, photographerList.getTotalElements());
    }

    private boolean hashTagCheck(List<String> photographerHashTags, List<String> conditionHashTags){
        return photographerHashTags.containsAll(conditionHashTags);
    }

    // 포토그래퍼 전체 조회
    public List<PhotographerResponseDto> findAll(){
        List<Photographer> photographerList = photographerRepository.findAll();
        List<PhotographerResponseDto> responsePhotographerDto = new ArrayList<>();
        photographerList.forEach(entity->{
            PhotographerResponseDto photographerResponseDto = entityToDto(entity);
            responsePhotographerDto.add(photographerResponseDto);
        });
        return responsePhotographerDto;
    }

    // 사진작가 내용 수정
    @Transactional
    public void photographerModify(PhotographerResponseDto photographerResponseDto){
        Photographer photographer = photographerRepository.findById(photographerResponseDto.getId()).orElseThrow(() -> new IllegalArgumentException());
        photographer.changeContent(photographerResponseDto.getContent());
    }

    private Photographer dtoToEntity(PhotographerRequestDto photographerRequestDto){
        String memberEmail = SecurityUtil.getCurrentMemberEmail(); // 사용자 email 반환
        if (photographerRepository.checkByPhotographerMember(memberEmail).isPresent())
            throw new IllegalStateException();
        Member member = memberRepository.findByEmail(memberEmail).get();
        Location location = Location.builder().id(photographerRequestDto.getLocationNum()).build();
        Photographer photographer = Photographer.builder()
                .member(member)
                .content(photographerRequestDto.getContent())
                .location(location)
                .build();
        return photographer;
    }

    private PhotographerResponseDto entityToDto(Photographer photographer){

        List<String> hashtagName=photographerHashTagRepository.getHashTagName(photographer.getId());
        List<String> urlList=photographerImageRepository.getUrl(photographer.getId());
        String location = photographer.getLocation().getName();

        PhotographerResponseDto photographerResponseDto = PhotographerResponseDto.builder()
                .id(photographer.getId())
                .content(photographer.getContent())
                .email(photographer.getMember().getEmail())
                .hashtag(hashtagName)
                .location(location)
                .photoImage(urlList)
                .nickName(photographer.getMember().getNickName())
                .build();

        return photographerResponseDto;
    }

}
