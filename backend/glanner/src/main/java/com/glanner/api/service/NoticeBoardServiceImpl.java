package com.glanner.api.service;

import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindNoticeBoardWithCommentResDto;
import com.glanner.api.exception.BoardNotFoundException;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.NoticeBoard;
import com.glanner.core.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeBoardServiceImpl implements NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public FindNoticeBoardWithCommentResDto getNotice(Long boardId) {
        NoticeBoard noticeBoard = noticeBoardRepository.findRealById(boardId).orElseThrow(BoardNotFoundException::new);
        noticeBoard.addCount();
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);
        return new FindNoticeBoardWithCommentResDto(noticeBoard, commentsByBoardId);
    }
}
