package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.dto.response.SaveGroupBoardResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.GroupBoardRepository;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class GroupBoardServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GlannerRepository glannerRepository;
    @Autowired
    private GroupBoardRepository groupBoardRepository;
    @Autowired
    private GroupBoardService groupBoardService;

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    public void testCreateGroupBoard() throws Exception{
        //given
        String userEmail = "cherish8513@naver.com";
        SaveGroupBoardReqDto reqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "null");

        //when
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        List<FileInfo> fileInfos = getFileInfos(reqDto.getFiles());
        GroupBoard groupBoard = reqDto.toEntity(user);
        groupBoard.changeFileInfo(fileInfos);
        Glanner glanner = Glanner.builder()
                .host(user)
                .build();
        Glanner savedGlanner = glannerRepository.save(glanner);

        groupBoard.changeGlanner(glanner);
        GroupBoard savedGroupBoard = groupBoardRepository.save(groupBoard);

        //then
        assertThat(savedGroupBoard.getGlanner().getId()).isEqualTo(savedGlanner.getId());
    }

    @Test
    public void testModifyGroupBoard() throws Exception{
        //given
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        GroupBoard groupBoard = GroupBoard.boardBuilder()
                .interests("#공부#")
                .user(user)
                .title("title")
                .content("content")
                .build();
        GroupBoard savedGroupBoard = groupBoardRepository.save(groupBoard);
        Long groupBoardId = savedGroupBoard.getId();
        SaveGroupBoardReqDto reqDto = new SaveGroupBoardReqDto("t", "c", new ArrayList<>(), "#휴식#");

        //when
        GroupBoard board = groupBoardRepository.findById(groupBoardId).orElseThrow(IllegalArgumentException::new);
        board.changeBoard(reqDto.getTitle(), reqDto.getContent(), getFileInfos(reqDto.getFiles()));
        board.changeInterests(reqDto.getInterests());

        //then
        GroupBoard findBoard = groupBoardRepository.findById(groupBoardId).orElseThrow(IllegalArgumentException::new);
        assertThat(findBoard.getTitle()).isEqualTo("t");
    }

    @Test
    public void testGetGlannerDetail() throws Exception{
        //given
        SaveGroupBoardResDto groupBoardResDto = groupBoardService.saveGroupBoard("cherish8513@naver.com", new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "interests"));


        //when
        FindGlannerResDto resDto = groupBoardService.getGlannerDetail(groupBoardResDto.getGroupBoardId());

        //then
        assertThat(resDto.getNumOfMember()).isEqualTo(1);
        assertThat(resDto.getHostEmail()).isEqualTo("cherish8513@naver.com");
    }

    public void createUser(){
        User user = User.builder()
                .phoneNumber("010-6575-2938")
                .email("cherish8513@naver.com")
                .name("JeongJooHeon")
                .password("1234")
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();

        DailyWorkSchedule workSchedule = DailyWorkSchedule.builder()
                .content("hard")
                .title("work")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(3))
                .build();
        schedule.addDailyWork(workSchedule);
        user.changeSchedule(schedule);
        userRepository.save(user);
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
