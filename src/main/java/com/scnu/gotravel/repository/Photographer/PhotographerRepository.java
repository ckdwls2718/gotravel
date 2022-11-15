package com.scnu.gotravel.repository.Photographer;

import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;

import com.scnu.gotravel.entity.Board;
import com.scnu.gotravel.entity.Member;
import com.scnu.gotravel.entity.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PhotographerRepository extends JpaRepository<Photographer, Long>, PhotographerRepositoryCustom{

    @Query("select p from Photographer p where p.member.email = :memberId")
    Optional<Photographer> checkByPhotographerMember(@Param("memberId") String memberId);

//    List<Photographer> search(PhotographerSearchCondition photographerSearchCondition);


//    @Query("select p from Photographer p join Member m on p.member.email=m.email where p.member.email=:membermId")
//    Optional<List<Photographer>> findByNickName(@Param("memberId")String nickName);


//    @Query("select b from Board b join Member m on b.member.id = m.id where b.member.email = :email")
//    Optional<List<Board>> findByMemberNickname(@Param("email") String email);


    @Query("select p.id from Photographer p where p.member.id= :memberId")
    Optional<List<Long>> findByMemberId(@Param("memberId")Long memberId);

}
