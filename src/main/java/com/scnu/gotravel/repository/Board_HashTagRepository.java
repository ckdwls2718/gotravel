package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Board_HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Board_HashTagRepository extends JpaRepository<Board_HashTag, Long> {

    @Query("select bh.hashTag.hashTagName from Board_HashTag bh where bh.board.id = :boardId")
    Optional<List<String>> getHashTagName(@Param("boardId")Long boardId);

    @Modifying
    @Query("delete from Board_HashTag bh where bh.board.id = :boardId")
    void deleteByBoardId(@Param("boardId")Long boardId);
}
