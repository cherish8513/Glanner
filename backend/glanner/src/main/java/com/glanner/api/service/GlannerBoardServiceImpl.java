package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
public class GlannerBoardServiceImpl implements GlannerBoardService{

    private final UserRepository userRepository;
    private final GlannerRepository glannerRepository;

    public void saveGlannerBoard(String userEmail, SaveGlannerBoardReqDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Glanner glanner = glannerRepository.findRealById(requestDto.getGlannerId()).orElseThrow(IllegalArgumentException::new);
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
