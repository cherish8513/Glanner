package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveFreeBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.dto.response.FindFreeBoardWithCommentsResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.FreeBoardService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/free-board")
public class FreeBoardController extends BoardController<SaveFreeBoardReqDto> {
    private final FreeBoardQueryRepository freeBoardQueryRepository;
    private final FreeBoardService freeBoardService;
    private final BoardService boardService;

    @Autowired
    public FreeBoardController(BoardService boardService, FreeBoardQueryRepository freeBoardQueryRepository, FreeBoardService freeBoardService) {
        super(boardService);
        this.boardService = boardService;
        this.freeBoardQueryRepository = freeBoardQueryRepository;
        this.freeBoardService = freeBoardService;
    }

    @PostMapping
    @ApiOperation(value = "게시판 저장")
    public ResponseEntity<BaseResponseEntity> saveBoard(@RequestBody @Valid SaveFreeBoardReqDto requestDto){
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        boardService.saveBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/like/{boardId}")
    @ApiOperation(value = "좋아요 증가")
    public ResponseEntity<BaseResponseEntity> addLikeBoards(@PathVariable Long boardId){
        freeBoardService.addLike(boardId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/dislike/{boardId}")
    @ApiOperation(value = "싫어요 증가")
    public ResponseEntity<BaseResponseEntity> addDislikeBoard(@PathVariable Long boardId){
        freeBoardService.addDislike(boardId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    /**
     *
     * @param page : 0부터 시작하며 데이터를 가져오는 시작 인덱스를 지정한다.
     * @param limit : 가져올 데이터의 갯수를 지정한다.
     * @return : 데이터의 정보를 Dto List 로 반환한다.
     */
    @GetMapping("/{page}/{limit}")
    @ApiOperation(value = "게시판 리스트 가져오기", notes = "page는 0부터 시작하며, limit은 가져올 게시판의 개수")
    public ResponseEntity<List<FindFreeBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{boardId}")
    @ApiOperation(value = "자유 게시판 가져오기", notes = "게시판의 정보 및 해당 게시판의 모든 댓글을 가져온다.")
    public ResponseEntity<FindFreeBoardWithCommentsResDto> getBoard(@PathVariable Long boardId){
        FindFreeBoardWithCommentsResDto responseDto = freeBoardService.getFreeBoard(boardId);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    @ApiOperation(value = "검색 게시판 리스트 가져오기", notes = "keyword가 제목 + 내용에 포함되어있는 게시판들을 가져온다.")
    public ResponseEntity<List<FindFreeBoardResDto>> searchBoards(@PathVariable int page, @PathVariable int limit, @RequestParam("keyword") String keyWord){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findPageWithKeyword(page, limit, keyWord);
        return ResponseEntity.status(200).body(responseDto);
    }
}
