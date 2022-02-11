package com.glanner.core.repository;

import com.glanner.core.domain.board.Comment;
import com.glanner.core.domain.board.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.glanner.core.domain.board.QComment.*;
import static com.glanner.core.domain.board.QFreeBoard.freeBoard;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Comment> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(comment)
                .from(comment)
                .innerJoin(comment.user, user)
                .innerJoin(comment.parent, comment)
                .where(comment.id.eq(id))
                .fetchOne());
    }
}
