package com.glanner.api.controller;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.UpdateCommentReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.ModifyCommentResDto;
import com.glanner.api.dto.response.SaveCommentResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.service.BoardService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * *Board Controller 부모 객체로 범용적인 CRUD를 담당한다.
 * @param <Q> : 각 Board Controller 에 맞는 Request dto 를 저장 및 수정에 이용하도록 Generic 으로 받는다.
 */
@RequiredArgsConstructor
public class BoardController<Q extends SaveBoardReqDto> {
    private final BoardService boardService;

    @PutMapping("/{boardId}")
    @ApiOperation(value = "게시판 수정")
    public ResponseEntity<BaseResponseEntity> modifyBoard(@PathVariable Long boardId, @RequestBody @Valid Q requestDto){
        boardService.modifyBoard(boardId, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/{boardId}")
    @ApiOperation(value = "게시판 삭제")
    public ResponseEntity<BaseResponseEntity> deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/comment")
    @ApiOperation(value = "댓글 추가", notes = "응답값으로 추가한 댓글을 반환한다.")
    public ResponseEntity<SaveCommentResDto> addComment(@RequestBody @Valid AddCommentReqDto reqDto){
        String userEmail = SecurityUtils.getCurrentUsername().orElseThrow(UserNotFoundException::new);
        SaveCommentResDto responseDto = boardService.addComment(userEmail, reqDto);
        return ResponseEntity.status(200).body(responseDto);
    }

    @PutMapping("/comment/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "응답값으로 수정한 댓글을 반환한다.")
    public ResponseEntity<ModifyCommentResDto> modifyComment(@PathVariable Long commentId, @RequestBody @Valid UpdateCommentReqDto reqDto){
        ModifyCommentResDto responseDto = boardService.modifyComment(commentId, reqDto);
        return ResponseEntity.status(200).body(responseDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public ResponseEntity<BaseResponseEntity> deleteComment(@PathVariable Long commentId){
        boardService.deleteComment(commentId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/download-file/{fileId}")
    public ResponseEntity<BaseResponseEntity> downloadFile(@PathVariable Long fileId){
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }
}
