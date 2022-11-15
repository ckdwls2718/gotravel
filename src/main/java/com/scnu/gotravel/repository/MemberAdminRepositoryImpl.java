package com.scnu.gotravel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scnu.gotravel.dto.MemberSearchCondition;
import com.scnu.gotravel.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.scnu.gotravel.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

public class MemberAdminRepositoryImpl implements MemberAdminRepository {

    private final JPAQueryFactory queryFactory;

    public MemberAdminRepositoryImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public Page<Member> search(MemberSearchCondition memberSearchCondition, Pageable pageable) {

        List<Member> result = queryFactory
                .select(member)
                .from(member)
                .where(
                        memberEmailLike(memberSearchCondition.getEmail()),
                        memberNickNameLike(memberSearchCondition.getNickname())
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        int total = queryFactory
                .select(member)
                .from(member)
                .where(
                        memberEmailLike(memberSearchCondition.getEmail()),
                        memberNickNameLike(memberSearchCondition.getNickname())
                )
                .distinct()
                .fetch().size();

        return new PageImpl<>(result,pageable,total);
    }
    private BooleanExpression memberEmailLike(String email) {
        return hasText(email) ? member.email.like(email) : null;
    }
    private BooleanExpression memberNickNameLike(String nickName) {
        return hasText(nickName) ? member.nickName.like(nickName) : null;
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();

        //sort
        sort.stream().forEach( order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String prop = order.getProperty();
                    PathBuilder orderByExpression = new PathBuilder(Member.class, "member");
                    orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
                }
        );
        return orders;
    }
}
