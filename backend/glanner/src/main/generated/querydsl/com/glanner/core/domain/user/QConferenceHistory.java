package com.glanner.core.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConferenceHistory is a Querydsl query type for ConferenceHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConferenceHistory extends EntityPathBase<ConferenceHistory> {

    private static final long serialVersionUID = 1224245442L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConferenceHistory conferenceHistory = new QConferenceHistory("conferenceHistory");

    public final com.glanner.core.domain.base.QBaseTimeEntity _super = new com.glanner.core.domain.base.QBaseTimeEntity(this);

    public final QConference conference;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final EnumPath<ConferenceHistoryStatus> status = createEnum("status", ConferenceHistoryStatus.class);

    public final QUser user;

    public QConferenceHistory(String variable) {
        this(ConferenceHistory.class, forVariable(variable), INITS);
    }

    public QConferenceHistory(Path<? extends ConferenceHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConferenceHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConferenceHistory(PathMetadata metadata, PathInits inits) {
        this(ConferenceHistory.class, metadata, inits);
    }

    public QConferenceHistory(Class<? extends ConferenceHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.conference = inits.isInitialized("conference") ? new QConference(forProperty("conference"), inits.get("conference")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

