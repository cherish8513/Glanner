package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GlannerBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GlannerBoardService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/glanner-board")
public class GlannerBoardController extends BoardController<SaveGlannerBoardReqDto>{
    private final GlannerBoardService glannerBoardService;
    private final GlannerBoardQueryRepository queryRepository;

    @Autowired
    public GlannerBoardController(BoardService boardService, GlannerBoardService glannerBoardService, GlannerBoardQueryRepository queryRepository) {
        super(boardService);
        this.glannerBoardService = glannerBoardService;
        this.queryRepository = queryRepository;
    }

    @PostMapping
    @ApiOperation(value = "게시판 저장")
    public ResponseEntity<BaseResponseEntity> saveBoard(@RequestBody @Valid SaveGlannerBoardReqDto requestDto) {
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        glannerBoardService.saveGlannerBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    /**
     * @param glannerId : 가져올 글래너 게시판이 속해있는 글래너의 ID
     * @param page : 0이 아닌 1부터 시작하며 데이터를 가져오는 시작 인덱스를 지정한다.
     * @param limit : 가져올 데이터의 갯수를 지정한다.
     * @return : 글래너 게시판 데이터의 정보를 Dto List 로 반환한다.
     */
    @GetMapping("{glannerId}/{page}/{limit}")
    @ApiOperation(value = "게시판 리스트 가져오기", notes = "page는 0부터 시작하며, limit은 가져올 게시판의 개수")
    public ResponseEntity<List<FindGlannerBoardWithCommentsResDto>> getBoards(@PathVariable Long glannerId, @PathVariable int page, @PathVariable int limit){
        List<FindGlannerBoardWithCommentsResDto> responseDto = glannerBoardService.getGlannerBoards(glannerId, page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{boardId}")
    @ApiOperation(value = "글래너 게시판 가져오기", notes = "게시판의 정보 및 해당 게시판의 모든 댓글을 가져온다.")
    public ResponseEntity<FindGlannerBoardWithCommentsResDto> getBoard(@PathVariable Long boardId){
        FindGlannerBoardWithCommentsResDto responseDto = glannerBoardService.getGlannerBoard(boardId);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("{glannerId}/search/{page}/{limit}")
    @ApiOperation(value = "검색 게시판 리스트 가져오기", notes = "keyword가 제목 + 내용에 포함되어있는 게시판들을 가져온다.")
    public ResponseEntity<List<FindGlannerBoardResDto>> searchBoards(@PathVariable Long glannerId, @PathVariable int page, @PathVariable int limit,  @RequestParam("keyword") String keyWord){
        List<FindGlannerBoardResDto> responseDto = queryRepository.findPageWithKeyword(glannerId, page, limit, keyWord);
        return ResponseEntity.status(200).body(responseDto);
    }
}
