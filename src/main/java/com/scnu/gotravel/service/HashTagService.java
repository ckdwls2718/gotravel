package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.board.ResponseBoardDto;
import com.scnu.gotravel.entity.Board;
import com.scnu.gotravel.entity.HashTag;
import com.scnu.gotravel.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    @Transactional
    public void save(String hashTagName) {
        hashTagRepository.save(HashTag.builder()
                .hashTagName(hashTagName)
                .build());
    }

//    public List<String> findAll(){
//
//
//    }
//  해시태그 추천
    public List<String> recommendHashTags(int count){

        List<String> hashTags = new ArrayList<>();
        hashTags.add(monthCheck());
        List<String> byHashTagIds = hashTagRepository.findByHashTagIds(randomHashTags(count));

        byHashTagIds.stream()
                .forEach(e -> hashTags.add(e));

        return hashTags;
    }

    private String monthCheck(){
        int monthValue = LocalDate.now().getMonthValue();
        if (monthValue >=3 && monthValue <=5){
            return "봄";
        } else if(monthValue >=6 && monthValue <=8){
            return "여름";
        } else if(monthValue >=9 && monthValue <=11){
            return "가을";
        } else{
            return "겨울";
        }
    }

    private List<Long> randomHashTags(int count){
        List<Long> hashTagId = new ArrayList<>();
        if(count>31){
            throw new RuntimeException("기존 해시태그의 값보다 더 큽니다.");
        }
        for(int i=0;i<count;i++){
            long a = (int)(Math.random()*31)+5;
            if(!hashTagId.contains(a))
                hashTagId.add(a);
            else
                i--;
        }
        return hashTagId;
    }


}
