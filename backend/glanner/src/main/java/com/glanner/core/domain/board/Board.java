package com.glanner.core.domain.board;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.glanner.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private  Long id;
    private String title;
    private String content;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.PERSIST)
    private List<FileInfo> fileInfos = new ArrayList<>();

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.count = 0;
    }


    public void changeBoard(String title, String content, List<FileInfo> fileInfos) {
        this.title = title;
        this.content = content;
        changeFileInfo(fileInfos);
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.changeBoard(this);
    }

    public void deleteComment(Comment comment){
        comments.remove(comment);
    }

    public void addCount(){
        this.count++;
    }

    public void changeFileInfo(List<FileInfo> fileInfos) {
       this.fileInfos = fileInfos;
    }
}

