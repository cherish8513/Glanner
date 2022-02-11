package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveFreeBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.dto.response.FindFreeBoardWithCommentsResDto;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.FreeBoardService;
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

    @Autowired
    public FreeBoardController(BoardService boardService, FreeBoardQueryRepository freeBoardQueryRepository, FreeBoardService freeBoardService) {
        super(boardService);
        this.freeBoardQueryRepository = freeBoardQueryRepository;
        this.freeBoardService = freeBoardService;
    }

    @PutMapping("/like/{id}")
    public ResponseEntity<BaseResponseEntity> addLikeBoards(@PathVariable Long id){
        freeBoardService.addLike(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/dislike/{id}")
    public ResponseEntity<BaseResponseEntity> addDislikeBoard(@PathVariable Long id){
        freeBoardService.addDislike(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    /**
     *
     * @param page : 0부터 시작하며 데이터를 가져오는 시작 인덱스를 지정한다.
     * @param limit : 가져올 데이터의 갯수를 지정한다.
     * @return : 데이터의 정보를 Dto List 로 반환한다.
     */
    @GetMapping("/{page}/{limit}")
    public ResponseEntity<List<FindFreeBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindFreeBoardWithCommentsResDto> getBoard(@PathVariable Long id){
        FindFreeBoardWithCommentsResDto responseDto = freeBoardService.getFreeBoard(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    public ResponseEntity<List<FindFreeBoardResDto>> searchBoards(@PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());
        return ResponseEntity.status(200).body(responseDto);
    }
}
