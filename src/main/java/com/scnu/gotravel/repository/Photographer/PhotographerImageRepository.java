package com.scnu.gotravel.repository.Photographer;


import com.scnu.gotravel.entity.PhotographerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotographerImageRepository extends JpaRepository<PhotographerImage, Long> {

    @Query("select p.imageURL from PhotographerImage p where p.photographer.id=:photographerId")
    List<String> getUrl(@Param("photographerId")Long photographerId);

}
