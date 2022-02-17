package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.*;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GroupBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GroupBoardService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
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

    @PostMapping
    @ApiOperation(value = "게시판 저장")
    public ResponseEntity<SaveGroupBoardResDto> saveGroupBoard(@RequestBody @Valid SaveGroupBoardReqDto requestDto) {
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        SaveGroupBoardResDto resDto = groupBoardService.saveGroupBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(resDto);
    }

    @Override
    @PutMapping("/{id}")
    @ApiOperation(value = "게시판 수정")
    public ResponseEntity<BaseResponseEntity> modifyBoard(@PathVariable Long id, @RequestBody @Valid SaveGroupBoardReqDto requestDto) {
        groupBoardService.modifyGroupBoard(id, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/{page}/{limit}")
    @ApiOperation(value = "게시판 리스트 가져오기", notes = "page는 0부터 시작하며, limit은 가져올 게시판의 개수")
    public ResponseEntity<List<FindGroupBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "특정 게시판 가져오기", notes = "게시판의 정보 및 해당 게시판의 모든 댓글을 가져온다.")
    public ResponseEntity<FindGroupBoardWithCommentResDto> getBoard(@PathVariable Long id){
        FindGroupBoardWithCommentResDto responseDto = groupBoardService.getGroupBoard(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/glanner/{boardId}")
    @ApiOperation(value = "글래너 가져오기", notes = "그룹 게시판과 연결된 글래너를 가져온다.")
    public ResponseEntity<FindGlannerResDto> searchBoardsWithInterest(@PathVariable Long boardId){
        FindGlannerResDto responseDto = groupBoardService.getGlannerDetail(boardId);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    @ApiOperation(value = "검색 게시판 리스트 가져오기", notes = "keyword가 제목 + 내용에 포함되어있는 게시판들을 가져온다.")
    public ResponseEntity<List<FindGroupBoardResDto>> searchBoardsWithKeyword(@PathVariable int page, @PathVariable int limit,  @RequestParam("keyword") String keyWord){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPageWithKeyword(page, limit, keyWord);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/interest/{page}/{limit}")
    @ApiOperation(value = "검색 게시판 리스트 가져오기", notes = "interest가 그룹 게시판 관심사에 포함되어있는 게시판들을 가져온다.")
    public ResponseEntity<List<FindGroupBoardResDto>> searchBoardsWithInterest(@PathVariable int page, @PathVariable int limit,  @RequestParam("keyword") String keyWord){
        List<FindGroupBoardResDto> responseDto = queryRepository.findPageWithInterest(page, limit, keyWord);
        return ResponseEntity.status(200).body(responseDto);
    }
}
