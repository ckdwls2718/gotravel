package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i.imageURL from Image i where i.board.id = :boardId")
    Optional<List<String>> getUrl (@Param("boardId")Long boardId);

    @Modifying
    @Query("delete from Image i where i.board.id = :boardId")
    void deleteByBoardId(@Param("boardId")Long boardId);
}
