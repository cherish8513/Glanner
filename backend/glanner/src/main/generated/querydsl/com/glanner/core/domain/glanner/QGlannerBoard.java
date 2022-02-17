package com.glanner.core.domain.glanner;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGlannerBoard is a Querydsl query type for GlannerBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGlannerBoard extends EntityPathBase<GlannerBoard> {

    private static final long serialVersionUID = 902389999L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGlannerBoard glannerBoard = new QGlannerBoard("glannerBoard");

    public final com.glanner.core.domain.board.QBoard _super;

    //inherited
    public final ListPath<com.glanner.core.domain.board.Comment, com.glanner.core.domain.board.QComment> comments;

    //inherited
    public final StringPath content;

    //inherited
    public final NumberPath<Integer> count;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final ListPath<com.glanner.core.domain.board.FileInfo, com.glanner.core.domain.board.QFileInfo> fileInfos;

    public final QGlanner glanner;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate;

    //inherited
    public final StringPath title;

    // inherited
    public final com.glanner.core.domain.user.QUser user;

    public QGlannerBoard(String variable) {
        this(GlannerBoard.class, forVariable(variable), INITS);
    }

    public QGlannerBoard(Path<? extends GlannerBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGlannerBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGlannerBoard(PathMetadata metadata, PathInits inits) {
        this(GlannerBoard.class, metadata, inits);
    }

    public QGlannerBoard(Class<? extends GlannerBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.glanner.core.domain.board.QBoard(type, metadata, inits);
        this.comments = _super.comments;
        this.content = _super.content;
        this.count = _super.count;
        this.createdDate = _super.createdDate;
        this.fileInfos = _super.fileInfos;
        this.glanner = inits.isInitialized("glanner") ? new QGlanner(forProperty("glanner"), inits.get("glanner")) : null;
        this.id = _super.id;
        this.modifiedDate = _super.modifiedDate;
        this.title = _super.title;
        this.user = _super.user;
    }

}

