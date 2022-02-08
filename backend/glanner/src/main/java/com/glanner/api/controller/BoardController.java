package com.glanner.api.controller;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.UpdateCommentReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.service.BoardService;
import com.glanner.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * *Board Controller 부모 객체로 범용적인 CRUD를 담당한다.
 * @param <Q> : 각 Board Controller 에 맞는 Request dto 를 저장 및 수정에 이용하도록 Generic 으로 받는다.
 */
@RequiredArgsConstructor
public class BoardController<Q extends SaveBoardReqDto> {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BaseResponseEntity> saveBoard(@RequestBody @Valid Q requestDto){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        boardService.saveBoard(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseEntity> modifyBoard(@PathVariable Long id, @RequestBody @Valid Q requestDto){
        boardService.modifyBoard(id, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseEntity> deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/comment")
    public ResponseEntity<BaseResponseEntity> addComment(@RequestBody @Valid AddCommentReqDto reqDto){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        boardService.addComment(userEmail, reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<BaseResponseEntity> modifyComment(@PathVariable Long id, @RequestBody @Valid UpdateCommentReqDto reqDto){
        boardService.modifyComment(id, reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<BaseResponseEntity> deleteComment(@PathVariable Long commentId){
        boardService.deleteComment(commentId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/download-file/{fileId}")
    public ResponseEntity<BaseResponseEntity> downloadFile(@PathVariable Long fileId){
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    public String getUsername(Optional<String> username){
        return username.orElseThrow(UserNotFoundException::new);
    }
}
