package com.scnu.gotravel.repository.Photographer;

import com.scnu.gotravel.entity.PhotographerHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotographerHashTagRepository extends JpaRepository<PhotographerHashTag, Long> {

    @Query("select ptl.photographerHashTagList.TagName from PhotographerHashTag ptl where ptl.photographer.id=:photoId")
    List<String> getHashTagName(@Param("photoId")Long photoId); // 과연 이부분에 optional로 해야할까? null 허용 관련 생각해보기
}