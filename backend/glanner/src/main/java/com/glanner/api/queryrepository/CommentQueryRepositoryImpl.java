package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindCommentResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.board.QComment.comment;

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
                        comment.user.name,
                        comment.user.email,
                        comment.content,
                        comment.createdDate)))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }

}
