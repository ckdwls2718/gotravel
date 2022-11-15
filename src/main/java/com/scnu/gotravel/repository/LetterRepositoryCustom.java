package com.scnu.gotravel.repository;

import com.scnu.gotravel.dto.LetterSearchCondition;
import com.scnu.gotravel.entity.Letter;

import java.util.List;

public interface LetterRepositoryCustom {
    List<Letter> search(String sendMember, int flag);
}
