package com.scnu.gotravel.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scnu.gotravel.dto.LetterSearchCondition;
import com.scnu.gotravel.entity.Letter;

import javax.persistence.EntityManager;
import java.util.List;

import static com.scnu.gotravel.entity.QLetter.letter;
import static org.springframework.util.StringUtils.*;

public class LetterRepositoryImpl implements LetterRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public LetterRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Letter> search(String email, int flag) {
        return queryFactory
                .select(letter)
                .from(letter)
                .where(
                        letterReach(email,flag)
                )
                .orderBy(letter.id.desc())
                .fetch();
    }

    private BooleanExpression letterReach(String email,int flag) {
        switch (flag){
            case 0:
                return letter.receiveMember.email.eq(email).or(letter.sendMember.email.eq(email));
            case 1:
                return letter.receiveMember.email.eq(email);
            case 2:
                return letter.sendMember.email.eq(email);
            default:
                return null;
        }
    }
}
