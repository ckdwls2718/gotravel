package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Board;
import com.scnu.gotravel.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Query("select h from HashTag h where h.id in :hashTag")
    List<HashTag> findByLongId(@Param("hashTag") List<Long> hashTag);

    @Query("select h.hashTagName from HashTag h where h.id in :hashTagIds")
    List<String> findByHashTagIds(@Param("hashTagIds") List<Long> hashTagIds);

    @Query("select b from Board b join Board_HashTag bh on bh.board.id = b.id where bh.hashTag.hashTagName =:hashTagName")
    List<Board> findByHashTagName(@Param("hashTagName") String hashTagName);
}
