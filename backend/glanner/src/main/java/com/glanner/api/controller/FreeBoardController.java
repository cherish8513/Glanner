package com.glanner.api.controller;

import com.glanner.api.dto.request.SaveFreeBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/free-board")
public class FreeBoardController extends BoardController<SaveFreeBoardReqDto> {
    private final FreeBoardQueryRepository freeBoardQueryRepository;

    @Autowired
    public FreeBoardController(BoardService boardService, FreeBoardQueryRepository freeBoardQueryRepository) {
        super(boardService);
        this.freeBoardQueryRepository = freeBoardQueryRepository;
    }

    /**
     *
     * @param page : 0이 아닌 1부터 시작하며 데이터를 가져오는 시작 인덱스를 지정한다.
     * @param limit : 가져올 데이터의 갯수를 지정한다.
     * @return : 데이터의 정보를 Dto List 로 반환한다.
     */
    @GetMapping("/{page}/{limit}")
    public ResponseEntity<List<FindFreeBoardResDto>> getBoards(@PathVariable int page, @PathVariable int limit){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findPage(page, limit);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindFreeBoardResDto> getBoard(@PathVariable Long id){
        FindFreeBoardResDto responseDto = freeBoardQueryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/search/{page}/{limit}")
    public ResponseEntity<List<FindFreeBoardResDto>> searchBoards(@PathVariable int page, @PathVariable int limit, @RequestBody @Valid SearchBoardReqDto reqDto){
        List<FindFreeBoardResDto> responseDto =freeBoardQueryRepository.findByKeyWord(page, limit, reqDto);
        return ResponseEntity.status(200).body(responseDto);
    }
}
