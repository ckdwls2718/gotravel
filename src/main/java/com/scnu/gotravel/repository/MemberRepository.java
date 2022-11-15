package com.scnu.gotravel.repository;

import com.scnu.gotravel.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,MemberAdminRepository {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickName(String nickname);

    // id 값 반환 email-> id
    @Query("select m.id from Member m where m.email=:email")
    String findEmail(@Param("email") String email);

    Optional<Member> findByEmailAndProvider(String email, String provider);

}
