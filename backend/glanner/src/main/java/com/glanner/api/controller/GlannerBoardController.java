package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GlannerBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GlannerBoardService;
import com.glanner.security.SecurityUtils;
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

    @Override
    @PostMapping
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
    public ResponseEntity<List<FindGlannerBoardResDto>> getBoards(@PathVariable Long glannerId, @PathVariable int page, @PathVariable int limit){
        List<FindGlannerBoardResDto> responseDto = queryRepository.findPage(glannerId, page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindGlannerBoardWithCommentsResDto> getBoard(@PathVariable Long id){
        FindGlannerBoardWithCommentsResDto responseDto = glannerBoardService.getGlannerBoard(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("{glannerId}/search/{page}/{limit}")
    public ResponseEntity<List<FindGlannerBoardResDto>> searchBoards(@PathVariable Long glannerId, @PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindGlannerBoardResDto> responseDto = queryRepository.findPageWithKeyword(glannerId, page, limit, reqDto.getKeyWord());
        return ResponseEntity.status(200).body(responseDto);
    }
}
