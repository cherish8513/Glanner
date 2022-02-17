package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.SaveNoticeBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindNoticeBoardResDto;
import com.glanner.api.dto.response.FindNoticeBoardWithCommentResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.NoticeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.NoticeBoardService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeBoardController extends BoardController<SaveBoardReqDto> {
    private final NoticeBoardQueryRepository queryRepository;
    private final NoticeBoardService noticeBoardService;
    private final BoardService boardService;

    @Autowired
    public NoticeBoardController(BoardService boardService, NoticeBoardQueryRepository queryRepository, NoticeBoardService noticeBoardService) {
        super(boardService);
        this.boardService = boardService;
        this.queryRepository = queryRepository;
        this.noticeBoardService = noticeBoardService;
    }

    @PostMapping
    @ApiOperation(value = "게시판 저장")
    public ResponseEntity<BaseResponseEntity> saveBoard(@RequestBody @Valid SaveNoticeBoardReqDto requestDto){
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        boardService.saveBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/{page}/{limit}")
    @ApiOperation(value = "게시판 리스트 가져오기", notes = "page는 0부터 시작하며, limit은 가져올 게시판의 개수")
    public ResponseEntity<List<FindNoticeBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindNoticeBoardResDto> responseDto = queryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "공지 게시판 가져오기", notes = "게시판의 정보 및 해당 게시판의 모든 댓글을 가져온다.")
    public ResponseEntity<FindNoticeBoardWithCommentResDto> getBoard(@PathVariable Long id){
        FindNoticeBoardWithCommentResDto responseDto = noticeBoardService.getNotice(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    @ApiOperation(value = "검색 게시판 리스트 가져오기", notes = "keyword가 제목 + 내용에 포함되어있는 게시판들을 가져온다.")
    public ResponseEntity<List<FindNoticeBoardResDto>> searchBoards(@PathVariable int page, @PathVariable int limit,  @RequestParam("keyword") String keyWord){
        List<FindNoticeBoardResDto> responseDto =queryRepository.findPageWithKeyword(page, limit, keyWord);
        return ResponseEntity.status(200).body(responseDto);
    }
}
