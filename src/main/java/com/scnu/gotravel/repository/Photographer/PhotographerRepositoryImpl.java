package com.scnu.gotravel.repository.Photographer;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scnu.gotravel.dto.photographer.PhotographerSearchCondition;
import com.scnu.gotravel.entity.Board;
import com.scnu.gotravel.entity.Photographer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.scnu.gotravel.entity.QPhotographer.photographer;
import static com.scnu.gotravel.entity.QPhotographerHashTag.photographerHashTag;
import static com.scnu.gotravel.entity.QPhotographerHashTagList.photographerHashTagList;
import static org.springframework.util.StringUtils.hasText;

public class PhotographerRepositoryImpl implements PhotographerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PhotographerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Photographer> search(PhotographerSearchCondition photographerSearchCondition, Pageable pageable) {
        List<Photographer> content = queryFactory
                .select(photographer)
                .from(photographerHashTag)
                .where(
                        photographerTagLike(photographerSearchCondition.getHashtag()),
                        photographerLocationLike(photographerSearchCondition.getLocation()),
                        photographerNickNameLike(photographerSearchCondition.getMemberNickname())
                )
                .join(photographerHashTag.photographer, photographer)
                .join(photographerHashTag.photographerHashTagList, photographerHashTagList)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        int total = queryFactory
                .select(photographer)
                .from(photographerHashTag)
                .where(
                        photographerTagLike(photographerSearchCondition.getHashtag()),
                        photographerLocationLike(photographerSearchCondition.getLocation()),
                        photographerNickNameLike(photographerSearchCondition.getMemberNickname())
                )
                .join(photographerHashTag.photographer, photographer)
                .join(photographerHashTag.photographerHashTagList, photographerHashTagList)
                .distinct()
                .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression photographerNickNameLike(String memberNickname){
        return hasText(memberNickname) ? photographerHashTag.photographer.member.nickName.like(memberNickname) : null;
    }

    private BooleanExpression photographerLocationLike(String location) {
        return hasText(location) ? photographerHashTag.photographer.location.name.eq(location): null;
    }

    private BooleanExpression photographerTagLike(List<String> hashtag) {
//        return hashtag.size()!=0 ? photographerHashTag.photographerHashTagList.TagName.in(hashtag): null;
//    }
        return !hashtag.isEmpty() ? photographerHashTag.photographerHashTagList.TagName.in(hashtag) : null;
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();
        //sort
        sort.stream().forEach( order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String prop = order.getProperty();
                    PathBuilder orderByExpression = new PathBuilder(Photographer.class, "photographer");
                    orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
                }
        );
        return orders;
    }
}
