package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindNoticeBoardResDto;
import com.glanner.api.dto.response.FindNoticeBoardWithCommentResDto;
import com.glanner.api.queryrepository.NoticeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.NoticeBoardService;
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

    @Autowired
    public NoticeBoardController(BoardService boardService, NoticeBoardQueryRepository queryRepository, NoticeBoardService noticeBoardService) {
        super(boardService);
        this.queryRepository = queryRepository;
        this.noticeBoardService = noticeBoardService;
    }

    @GetMapping("/{page}/{limit}")
    public ResponseEntity<List<FindNoticeBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindNoticeBoardResDto> responseDto = queryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindNoticeBoardWithCommentResDto> getBoard(@PathVariable Long id){
        FindNoticeBoardWithCommentResDto responseDto = noticeBoardService.getNotice(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    public ResponseEntity<List<FindNoticeBoardResDto>> searchBoards(@PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindNoticeBoardResDto> responseDto =queryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());
        return ResponseEntity.status(200).body(responseDto);
    }
}
