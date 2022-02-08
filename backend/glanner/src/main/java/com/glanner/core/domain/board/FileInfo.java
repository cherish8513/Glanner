package com.glanner.core.domain.board;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo {

    @Builder
    public FileInfo(String saveFolder, String originFile, String saveFile, Board board) {
        this.saveFolder = saveFolder;
        this.originFile = originFile;
        this.saveFile = saveFile;
        this.board = board;
    }

    @Id
    @GeneratedValue
    @Column(name = "fileInfo_id")
    private Long id;
    private String saveFolder;
    private String originFile;
    private String saveFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
