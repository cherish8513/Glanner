package com.glanner.api.service;

import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindFreeBoardWithCommentsResDto;
import com.glanner.api.exception.BoardNotFoundException;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService {
    private final FreeBoardRepository freeBoardRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public FindFreeBoardWithCommentsResDto getFreeBoard(Long boardId) {
        FreeBoard freeBoard = freeBoardRepository.findRealById(boardId).orElseThrow(BoardNotFoundException::new);
        freeBoard.addCount();
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);
        return new FindFreeBoardWithCommentsResDto(freeBoard, commentsByBoardId);
    }

    @Override
    public void addLike(Long boardId) {
        FreeBoard freeBoard = freeBoardRepository.findRealById(boardId).orElseThrow(BoardNotFoundException::new);
        freeBoard.addLike();
    }

    @Override
    public void addDislike(Long boardId) {
        FreeBoard freeBoard = freeBoardRepository.findRealById(boardId).orElseThrow(BoardNotFoundException::new);
        freeBoard.addDislike();
    }
}
