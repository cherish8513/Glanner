package com.glanner.core.repository;

import com.glanner.core.domain.user.UserConference;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.user.QUserConference.userConference;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserConferenceCustomRepositoryImpl implements UserConferenceCustomRepository{

    private  final JPAQueryFactory query;

    @Override
    public List<UserConference> findByConferenceId(Long conferenceId) {
        return query
                .select(userConference)
                .from(userConference)
                .where(userConference.conference.id.eq(conferenceId))
                .fetch();
    }
}
