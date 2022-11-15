package com.scnu.gotravel.service;

import com.scnu.gotravel.dto.*;
import com.scnu.gotravel.entity.HashTag;
import com.scnu.gotravel.entity.Location;
import com.scnu.gotravel.entity.PhotographerHashTagList;
import com.scnu.gotravel.repository.HashTagRepository;
import com.scnu.gotravel.repository.LocationRepository;
import com.scnu.gotravel.repository.Photographer.PhotographerHashTagListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Search {

    private final LocationRepository locationRepository;
    private final HashTagRepository hashTagRepository;
    private final PhotographerHashTagListRepository photographerHashTagListRepository;

    public LocationSearchDto showBoardLocationAndHashTag(){
        List<LocationDto> resultLocation1 = new ArrayList<>();
        List<LocationDto> resultLocation2 = new ArrayList<>();
        List<HashTagDto> resultHashTag = new ArrayList<>();
        List<Location> findLocation1 = locationRepository.findAll1();
        List<Location> findLocation2 = locationRepository.findAll2();
        List<HashTag> findHashTag = hashTagRepository.findAll();

        for(Location location : findLocation1){
            LocationDto locationDto = LocationDto.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .parentId(location.getParent().getId())
                    .build();
            resultLocation1.add(locationDto);

        }

        for(Location location : findLocation2){
            LocationDto locationDto = LocationDto.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .build();
            resultLocation2.add(locationDto);
        }

        for(HashTag hashTag : findHashTag){
            HashTagDto hashTagDto = HashTagDto.builder()
                    .id(hashTag.getId())
                    .name(hashTag.getHashTagName())
                    .build();
            resultHashTag.add(hashTagDto);
        }
        LocationSearchDto locationSearchDto = LocationSearchDto.builder()
                .ParentLocationList(resultLocation2)
                .ChildLocationList(resultLocation1)
                .hashTagList(resultHashTag)
                .build();

        return locationSearchDto;
    }

    public PhotographerSearchDto showPhotographerLocationAndHashTag(){
        List<LocationDto> resultLocation1 = new ArrayList<>();
        List<LocationDto> resultLocation2 = new ArrayList<>();
        List<PhotographerHashTagSearchDto> resultPhotographerHashTag = new ArrayList<>();
        List<Location> findLocation1 = locationRepository.findAll1();
        List<Location> findLocation2 = locationRepository.findAll2();
        List<PhotographerHashTagList> findHashTag = photographerHashTagListRepository.findAll();

        for(Location location : findLocation1){
            LocationDto locationDto = LocationDto.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .parentId(location.getParent().getId())
                    .build();
            resultLocation1.add(locationDto);

        }

        for(Location location : findLocation2){
            LocationDto locationDto = LocationDto.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .build();
            resultLocation2.add(locationDto);
        }

        for(PhotographerHashTagList hashTag : findHashTag){
            PhotographerHashTagSearchDto photographerHashTagSearchDto = PhotographerHashTagSearchDto.builder()
                    .id(hashTag.getId())
                    .name(hashTag.getTagName())
                    .build();
            resultPhotographerHashTag.add(photographerHashTagSearchDto);
        }
        PhotographerSearchDto photographerSearchDto = PhotographerSearchDto.builder()
                .photographerHashTagSearchList(resultPhotographerHashTag)
                .ParentLocationList(resultLocation2)
                .ChildLocationList(resultLocation1)
                .build();

        return photographerSearchDto;
    }
}
