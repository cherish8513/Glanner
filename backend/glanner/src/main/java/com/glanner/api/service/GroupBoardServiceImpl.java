package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;
import com.glanner.api.exception.BoardNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.GroupBoardRepository;
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
public class GroupBoardServiceImpl implements GroupBoardService {

    private final UserRepository userRepository;
    private final GlannerRepository glannerRepository;
    private final GroupBoardRepository groupBoardRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public Long saveGroupBoard(String userEmail, SaveGroupBoardReqDto reqDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        List<FileInfo> fileInfos = getFileInfos(reqDto.getFiles());
        GroupBoard groupBoard = reqDto.toEntity(user);
        groupBoard.changeFileInfo(fileInfos);
        Glanner glanner = Glanner.builder()
                .host(user)
                .build();
        glannerRepository.save(glanner);

        groupBoard.changeGlanner(glanner);
        return groupBoardRepository.save(groupBoard).getId();
    }

    @Override
    public void modifyGroupBoard(Long boardId, SaveGroupBoardReqDto reqDto) {
        GroupBoard board = groupBoardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        board.changeBoard(reqDto.getTitle(), reqDto.getContent(), getFileInfos(reqDto.getFiles()));
        board.changeInterests(reqDto.getInterests());
    }

    @Override
    public FindGroupBoardWithCommentResDto getGroupBoard(Long boardId) {
        GroupBoard groupBoard = groupBoardRepository.findRealById(boardId).orElseThrow(BoardNotFoundException::new);
        groupBoard.addCount();
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);
        return new FindGroupBoardWithCommentResDto(groupBoard, commentsByBoardId);
    }


    private List<FileInfo> getFileInfos(List<MultipartFile> files) {
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
