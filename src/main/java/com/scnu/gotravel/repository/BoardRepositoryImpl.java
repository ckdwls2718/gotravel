package com.scnu.gotravel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scnu.gotravel.dto.BoardSearchCondition;
import com.scnu.gotravel.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.scnu.gotravel.entity.QBoard.board;
import static com.scnu.gotravel.entity.QBoard_HashTag.board_HashTag;
import static com.scnu.gotravel.entity.QHashTag.*;
import static org.springframework.util.StringUtils.hasText;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> search(BoardSearchCondition boardSearchCondition, Pageable pageable) {

        List<Board> content = queryFactory
                .select(board)
                .from(board_HashTag)
                .where(
                        boardTitleLike(boardSearchCondition.getBoardTitle()),
                        boardHashTagLike(boardSearchCondition.getHashTags()),
                        memberNicknameLike(boardSearchCondition.getMemberNickname()),
                        boardLocationEq(boardSearchCondition.getLocation())
                )
                .leftJoin(board_HashTag.board, board)
                .leftJoin(board_HashTag.hashTag, hashTag)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        int total = queryFactory
                .select(board)
                .from(board_HashTag)
                .where(
                        boardTitleLike(boardSearchCondition.getBoardTitle()),
                        boardHashTagLike(boardSearchCondition.getHashTags()),
                        memberNicknameLike(boardSearchCondition.getMemberNickname()),
                        boardLocationEq(boardSearchCondition.getLocation())
                )
                .leftJoin(board_HashTag.board, board)
                .leftJoin(board_HashTag.hashTag, hashTag)
                .distinct()
                .fetch().size();

        return new PageImpl<>(content,pageable,total);
    }
    private BooleanExpression boardTitleLike(String boardTitle) {
        return hasText(boardTitle) ? board_HashTag.board.title.like(boardTitle) : null;
    }

    private BooleanExpression boardHashTagLike(List<String> hashTags) {
        return  !hashTags.isEmpty() ? board_HashTag.hashTag.hashTagName.in(hashTags) : null;
    }

    private BooleanExpression memberNicknameLike(String memberNickname) {
        return hasText(memberNickname) ? board_HashTag.board.member.nickName.like(memberNickname) : null;
    }

    private BooleanExpression boardLocationEq(String location) {
        return hasText(location) ? board_HashTag.board.location.name.like(location) : null;
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();

        //sort
        sort.stream().forEach( order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String prop = order.getProperty();
                    PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
                    orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
                }
        );
        return orders;
    }
}
