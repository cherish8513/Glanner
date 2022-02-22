package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.core.domain.board.QBoard;
import com.glanner.core.domain.user.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.board.QBoard.*;
import static com.glanner.core.domain.board.QComment.comment;
import static com.glanner.core.domain.user.QUser.*;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryRepositoryImpl implements CommentQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindCommentResDto> findCommentsByBoardId(Long boardId) {
        return query
                .select((Projections.constructor(FindCommentResDto.class,
                        comment.id,
                        comment.parent.id,
                        user.name,
                        user.email,
                        comment.content,
                        comment.createdDate)))
                .from(comment)
                .join(comment.board, board)
                .join(comment.user, user)
                .where(board.id.eq(boardId))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }

}
