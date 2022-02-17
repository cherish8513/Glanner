package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;
import com.glanner.api.exception.GlannerNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.GlannerBoardRepository;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GlannerBoardServiceImpl implements GlannerBoardService{

    private final UserRepository userRepository;
    private final GlannerRepository glannerRepository;
    private final GlannerBoardRepository glannerBoardRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public List<FindGlannerBoardWithCommentsResDto> getGlannerBoards(Long glannerId, int offset, int limit) {
        List<FindGlannerBoardWithCommentsResDto> resDto = new ArrayList<>();
        List<GlannerBoard> boards = glannerBoardRepository.findPage(glannerId, offset, limit);
        for(GlannerBoard board : boards){
            List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(board.getId());
            resDto.add(new FindGlannerBoardWithCommentsResDto(board, commentsByBoardId));
        }
        return resDto;
    }

    @Override
    public FindGlannerBoardWithCommentsResDto getGlannerBoard(Long boardId) {
        GlannerBoard glannerBoard = glannerBoardRepository.findRealById(boardId).orElseThrow(GlannerNotFoundException::new);
        glannerBoard.addCount();
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);
        return new FindGlannerBoardWithCommentsResDto(glannerBoard, commentsByBoardId);
    }

    public Long saveGlannerBoard(String userEmail, SaveGlannerBoardReqDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Glanner glanner = glannerRepository.findRealById(requestDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);
        List<FileInfo> fileInfos = getFileInfos(requestDto.getFiles());

        GlannerBoard board = GlannerBoard
                .boardBuilder()
                .glanner(glanner)
                .user(user)
                .content(requestDto.getContent())
                .title(requestDto.getTitle())
                .build();

        board.changeFileInfo(fileInfos);

        glanner.addGlannerBoard(board);
        return glannerBoardRepository.save(board).getId();
    }

    protected List<FileInfo> getFileInfos(List<MultipartFile> files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        if(!files.isEmpty()){
            String realPath = "uploads/";
            String date = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = realPath + File.separator + date;

            File folder = new File(saveFolder);

            if(!folder.exists()) folder.mkdir();
            for(MultipartFile file: files){
                String originalFileName = file.getOriginalFilename();
                FileInfo fileInfo = null;
                if(!originalFileName.isEmpty()){
                    String saveFileName = UUID.randomUUID().toString()
                            + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    fileInfo = FileInfo.builder()
                            .saveFolder(date)
                            .originFile(originalFileName)
                            .saveFile(saveFileName).build();
                    try {
                        file.transferTo(new File(folder, saveFileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }
}
