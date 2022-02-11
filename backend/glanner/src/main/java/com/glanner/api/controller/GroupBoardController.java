package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindGroupBoardResDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GroupBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GroupBoardService;
import com.glanner.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/group-board")
public class GroupBoardController extends BoardController<SaveGroupBoardReqDto> {
    private final GroupBoardQueryRepository queryRepository;
    private final GroupBoardService groupBoardService;

    @Autowired
    public GroupBoardController(BoardService boardService, GroupBoardQueryRepository queryRepository, GroupBoardService groupBoardService) {
        super(boardService);
        this.queryRepository = queryRepository;
        this.groupBoardService = groupBoardService;
    }

    @Override
    @PostMapping
    public ResponseEntity<BaseResponseEntity> saveBoard(@RequestBody @Valid SaveGroupBoardReqDto requestDto) {
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        groupBoardService.saveGroupBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseEntity> modifyBoard(@PathVariable Long id, @RequestBody @Valid SaveGroupBoardReqDto requestDto) {
        groupBoardService.modifyGroupBoard(id, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/{page}/{limit}")
    public ResponseEntity<List<FindGroupBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindGroupBoardWithCommentResDto> getBoard(@PathVariable Long id){
        FindGroupBoardWithCommentResDto responseDto = groupBoardService.getGroupBoard(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    public ResponseEntity<List<FindGroupBoardResDto>> searchBoardsWithKeyword(@PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/interest/{page}/{limit}")
    public ResponseEntity<List<FindGroupBoardResDto>> searchBoardsWithInterest(@PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPageWithInterest(page, limit, reqDto.getKeyWord());
        return ResponseEntity.status(200).body(responseDto);
    }
}
