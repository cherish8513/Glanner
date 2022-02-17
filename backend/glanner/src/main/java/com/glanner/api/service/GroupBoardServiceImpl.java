package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;
import com.glanner.api.dto.response.SaveGroupBoardResDto;
import com.glanner.api.exception.BoardNotFoundException;
import com.glanner.api.exception.GlannerNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.GroupBoardRepository;
import com.glanner.core.repository.UserGlannerRepository;
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
    private final GroupBoardRepository groupBoardRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final GlannerRepository glannerRepository;
    private final UserGlannerRepository userGlannerRepository;

    public SaveGroupBoardResDto saveGroupBoard(String userEmail, SaveGroupBoardReqDto reqDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        List<FileInfo> fileInfos = getFileInfos(reqDto.getFiles());
        GroupBoard groupBoard = reqDto.toEntity(user);
        groupBoard.changeFileInfo(fileInfos);

        Glanner glanner = Glanner.builder()
                .name(user.getName() + "님의 글래너")
                .host(user)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(user)
                .build();

        glanner.addUserGlanner(userGlanner);
        Glanner savedGlanner = glannerRepository.save(glanner);

        groupBoard.changeGlanner(glanner);
        GroupBoard savedGroupBoard = groupBoardRepository.save(groupBoard);

        return new SaveGroupBoardResDto(savedGroupBoard.getId(), savedGlanner.getId(), reqDto.getTitle());
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

    @Override
    public FindGlannerResDto getGlannerDetail(Long boardId) {
        GroupBoard board = groupBoardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Long id = board.getGlanner().getId();
        Glanner findGlanner = glannerRepository.findRealById(id).orElseThrow(GlannerNotFoundException::new);
        List<UserGlanner> findUserGlanners = userGlannerRepository.findByGlannerId(id);
        return new FindGlannerResDto(findGlanner, board, findUserGlanners);
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
