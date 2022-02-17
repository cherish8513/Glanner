package com.glanner.core.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConference is a Querydsl query type for Conference
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConference extends EntityPathBase<Conference> {

    private static final long serialVersionUID = -485615758L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConference conference = new QConference("conference");

    public final com.glanner.core.domain.base.QBaseTimeEntity _super = new com.glanner.core.domain.base.QBaseTimeEntity(this);

    public final NumberPath<Integer> attendLimit = createNumber("attendLimit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> callEndTime = createDateTime("callEndTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> callStartTime = createDateTime("callStartTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final com.glanner.core.domain.glanner.QGlanner glanner;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ConferenceActiveStatus> isActive = createEnum("isActive", ConferenceActiveStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QUser owner;

    public QConference(String variable) {
        this(Conference.class, forVariable(variable), INITS);
    }

    public QConference(Path<? extends Conference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConference(PathMetadata metadata, PathInits inits) {
        this(Conference.class, metadata, inits);
    }

    public QConference(Class<? extends Conference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.glanner = inits.isInitialized("glanner") ? new com.glanner.core.domain.glanner.QGlanner(forProperty("glanner"), inits.get("glanner")) : null;
        this.owner = inits.isInitialized("owner") ? new QUser(forProperty("owner"), inits.get("owner")) : null;
    }

}

