package com.scnu.gotravel.repository;

import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<Board> search(BoardSearchCondition boardSearchCondition, Pageable pageable);

}
