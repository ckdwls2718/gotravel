package com.scnu.gotravel.repository;

import com.scnu.gotravel.dto.MemberSearchCondition;
import com.scnu.gotravel.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAdminRepository {
    Page<Member> search(MemberSearchCondition memberSearchCondition, Pageable pageable);
}
