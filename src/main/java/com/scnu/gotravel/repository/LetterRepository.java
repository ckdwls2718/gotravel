package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long>, LetterRepositoryCustom {

    @Query("select l from Letter l where l.sendMember.id = :member or l.receiveMember.id = :member")
    Optional<List<Letter>> findByAllByMember(@Param("member") String member);

    @Query("select l from Letter l where l.sendMember.email = :email")
    Optional<List<Letter>> findByAllBySendMemberEmail(@Param("email") String email);

    @Query("select l from Letter l where l.receiveMember.email = :email")
    Optional<List<Letter>> findByAllByReceiveMemberEmail(@Param("email") String email);

    @Query("select l.id from Letter l where l.receiveMember.id = :memberId")
    Optional<List<Long>> findByReceiveMemberId(@Param("memberId")Long memberId);

    @Query("select l.id from Letter l where l.sendMember.id = :memberId")
    Optional<List<Long>> findBySendMemberId(@Param("memberId")Long memberId);
}
