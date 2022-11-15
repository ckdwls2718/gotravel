package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Board;
import com.scnu.gotravel.entity.Recommend;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select r from Recommend r where r.board.id = :boardId and r.member.email = :email")
    Optional<Recommend> findByBoardAndMember(@Param("boardId") Long boardId, @Param("email") String email);

    @Query("select r from Recommend r where r.member.id = :memberId")
    @EntityGraph(attributePaths = {"board"})
    List<Recommend> findByMemberId(@Param("memberId") String memberId);

    @Query("select b from Board b join fetch Recommend r on b.id = r.board.id where r.member.email = :email")
    List<Board> findByMemberEmail(@Param("email") String email);

    @Query("select r.id from Recommend r where r.board.id = :boardId")
    Optional<List<Long>> findByBoardId(@Param("boardId")Long boardId);
}
