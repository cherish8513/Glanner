package com.glanner.core.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeBoard is a Querydsl query type for NoticeBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNoticeBoard extends EntityPathBase<NoticeBoard> {

    private static final long serialVersionUID = 2111001401L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeBoard noticeBoard = new QNoticeBoard("noticeBoard");

    public final QBoard _super;

    //inherited
    public final ListPath<Comment, QComment> comments;

    //inherited
    public final StringPath content;

    //inherited
    public final NumberPath<Integer> count;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final ListPath<FileInfo, QFileInfo> fileInfos;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate;

    //inherited
    public final StringPath title;

    // inherited
    public final com.glanner.core.domain.user.QUser user;

    public QNoticeBoard(String variable) {
        this(NoticeBoard.class, forVariable(variable), INITS);
    }

    public QNoticeBoard(Path<? extends NoticeBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeBoard(PathMetadata metadata, PathInits inits) {
        this(NoticeBoard.class, metadata, inits);
    }

    public QNoticeBoard(Class<? extends NoticeBoard> type, PathMetadata metadata, PathInits inits) {
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

