package com.glanner.core.domain.glanner;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGlanner is a Querydsl query type for Glanner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGlanner extends EntityPathBase<Glanner> {

    private static final long serialVersionUID = 96479287L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGlanner glanner = new QGlanner("glanner");

    public final com.glanner.core.domain.base.QBaseTimeEntity _super = new com.glanner.core.domain.base.QBaseTimeEntity(this);

    public final QGroupBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<DailyWorkGlanner, QDailyWorkGlanner> dailyworks = this.<DailyWorkGlanner, QDailyWorkGlanner>createList("dailyworks", DailyWorkGlanner.class, QDailyWorkGlanner.class, PathInits.DIRECT2);

    public final ListPath<GlannerBoard, QGlannerBoard> glannerBoards = this.<GlannerBoard, QGlannerBoard>createList("glannerBoards", GlannerBoard.class, QGlannerBoard.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<UserGlanner, QUserGlanner> userGlanners = this.<UserGlanner, QUserGlanner>createList("userGlanners", UserGlanner.class, QUserGlanner.class, PathInits.DIRECT2);

    public QGlanner(String variable) {
        this(Glanner.class, forVariable(variable), INITS);
    }

    public QGlanner(Path<? extends Glanner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGlanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGlanner(PathMetadata metadata, PathInits inits) {
        this(Glanner.class, metadata, inits);
    }

    public QGlanner(Class<? extends Glanner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QGroupBoard(forProperty("board"), inits.get("board")) : null;
    }

}

