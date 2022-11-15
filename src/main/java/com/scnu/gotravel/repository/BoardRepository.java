package com.scnu.gotravel.repository;

import com.scnu.gotravel.dto.ResponseMapDto;
import com.scnu.gotravel.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    @Query("select b from Board b join Recommend r on b.id = r.board.id where r.id = :recommendId ")
    Optional<Board> findByRecommendId(@Param("recommendId") Long recommendId);

    @Query("select b from Board b join Member m on b.member.id = m.id where b.member.email = :email")
    Optional<List<Board>> findByMemberNickname(@Param("email") String email);

    @Query("select b from Board b order by b.recommendCount desc , b.createDate desc")
    Optional<List<Board>> findByRank();

    @Query("select b from Board b join Board_HashTag bh on b.id = bh.board.id where bh.hashTag.hashTagName in :hashTags")
    Optional<List<Board>> findByHashTags(@Param("hashTags") List<String> hashTags);

    @Query("select new com.scnu.gotravel.dto.ResponseMapDto(l.name ,count(b), l.parent.name) from Board b join Location l on b.location.id = l.id group by l.id")
    Optional<List<ResponseMapDto>> countByLocation();

    Page<Board> findAllBy(Pageable pageable);

    @Query("select b.id from Board b where b.member.id = :memberId")
    Optional<List<Long>> findByMemberId(@Param("memberId")Long memberId);

    @Query("select b from Board b where b.location.id = :locationNum")
    Optional<List<Board>> findByLocationNum(@Param("locationNum")Long locationNum);

}
