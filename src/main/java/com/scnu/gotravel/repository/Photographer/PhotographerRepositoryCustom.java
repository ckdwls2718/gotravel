package com.scnu.gotravel.repository.Photographer;


import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;
import com.scnu.gotravel.entity.Photographer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotographerRepositoryCustom {

    Page<Photographer> search(PhotographerSearchCondition photographerSearchCondition, Pageable pageable);
}
