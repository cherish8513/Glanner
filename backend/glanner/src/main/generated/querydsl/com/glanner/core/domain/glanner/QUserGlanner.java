package com.glanner.core.domain.glanner;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserGlanner is a Querydsl query type for UserGlanner
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserGlanner extends EntityPathBase<UserGlanner> {

    private static final long serialVersionUID = 464552684L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserGlanner userGlanner = new QUserGlanner("userGlanner");

    public final com.glanner.core.domain.base.QBaseTimeEntity _super = new com.glanner.core.domain.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QGlanner glanner;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.glanner.core.domain.user.QUser user;

    public QUserGlanner(String variable) {
        this(UserGlanner.class, forVariable(variable), INITS);
    }

    public QUserGlanner(Path<? extends UserGlanner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserGlanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserGlanner(PathMetadata metadata, PathInits inits) {
        this(UserGlanner.class, metadata, inits);
    }

    public QUserGlanner(Class<? extends UserGlanner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.glanner = inits.isInitialized("glanner") ? new QGlanner(forProperty("glanner"), inits.get("glanner")) : null;
        this.user = inits.isInitialized("user") ? new com.glanner.core.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

