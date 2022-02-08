package com.glanner.core.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoard is a Querydsl query type for FreeBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFreeBoard extends EntityPathBase<FreeBoard> {

    private static final long serialVersionUID = -41810203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoard freeBoard = new QFreeBoard("freeBoard");

    public final QBoard _super;

    //inherited
    public final ListPath<Comment, QComment> comments;

    //inherited
    public final StringPath content;

    //inherited
    public final NumberPath<Integer> count;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final NumberPath<Integer> dislikeCount = createNumber("dislikeCount", Integer.class);

    //inherited
    public final ListPath<FileInfo, QFileInfo> fileInfos;

    //inherited
    public final NumberPath<Long> id;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate;

    //inherited
    public final StringPath title;

    // inherited
    public final com.glanner.core.domain.user.QUser user;

    public QFreeBoard(String variable) {
        this(FreeBoard.class, forVariable(variable), INITS);
    }

    public QFreeBoard(Path<? extends FreeBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoard(PathMetadata metadata, PathInits inits) {
        this(FreeBoard.class, metadata, inits);
    }

    public QFreeBoard(Class<? extends FreeBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBoard(type, metadata, inits);
        this.comments = _super.comments;
        this.content = _super.content;
        this.count = _super.count;
        this.createdDate = _super.createdDate;
        this.fileInfos = _super.fileInfos;
        this.id = _super.id;
        this.modifiedDate = _super.modifiedDate;
        this.title = _super.title;
        this.user = _super.user;
    }

}

