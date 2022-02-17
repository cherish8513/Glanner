package com.glanner.core.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyWorkSchedule is a Querydsl query type for DailyWorkSchedule
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDailyWorkSchedule extends EntityPathBase<DailyWorkSchedule> {

    private static final long serialVersionUID = 675017323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyWorkSchedule dailyWorkSchedule = new QDailyWorkSchedule("dailyWorkSchedule");

    public final com.glanner.core.domain.base.QBaseEntity _super = new com.glanner.core.domain.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> alarmDate = _super.alarmDate;

    //inherited
    public final EnumPath<ConfirmStatus> confirmStatus = _super.confirmStatus;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QSchedule schedule;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public QDailyWorkSchedule(String variable) {
        this(DailyWorkSchedule.class, forVariable(variable), INITS);
    }

    public QDailyWorkSchedule(Path<? extends DailyWorkSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyWorkSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyWorkSchedule(PathMetadata metadata, PathInits inits) {
        this(DailyWorkSchedule.class, metadata, inits);
    }

    public QDailyWorkSchedule(Class<? extends DailyWorkSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

