import axios from "axios";
import jwt_decode from "jwt-decode";
import moment from "moment";
import React from "react"
import { useState } from "react";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { GlannerBoardPresenter } from "./GlannerBoardPresenter"
import InfiniteScroll from "react-infinite-scroll-component";

export default function GroupPlannerContainer () {
    const [boardList, setBoardList] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(true);
    const { id } = useParams();
    const { register, watch, formState: {errors}, handleSubmit, reset } = useForm();
    const [decoded, setDecoded] = useState('');
    const [fetching, setFetching] = useState(false);
    // 무한 스크롤
    const handleScroll = (nowPage) => {
        if (nowPage > boardList.length) {
            setFetching(true)
            setLoading(false)           
            return
        }
        setLoading(true)
        const { scrollTop, clientHeight, scrollHeight } = document.documentElement;
        // console.log(
        //     Math.round(scrollHeight - scrollTop),  Math.round(clientHeight)
        // );
        if ((Math.round(scrollHeight - scrollTop) < Math.round(clientHeight) + 50) && fetching === false) {
            setPage(prev => {
                return prev + 5
            });
            setLoading(false)
        }
      };
    
    useEffect(() => {
        setFetching(true);
        setLoading(true)
        axios(`/api/glanner-board/${id}/${page}/5`)
            .then(res => {
                setBoardList((prev) => {
                    // console.log([...prev, ...res.data])
                    return [...prev, ...res.data]
                })
                setLoading(false)
            })
            .catch(err => console.log(err))
        
        setDecoded(jwt_decode(localStorage.getItem('token')))
        setFetching(false);
        window.addEventListener('scroll', (event) => {
            if (page < boardList.length) {
                return
            } else {
                handleScroll(page)
            }
        })
        return () => {
          // scroll event listener 해제
          window.removeEventListener("scroll", handleScroll);
        };
        }, [page, id])
    
    // 글래너 게시글 작성
    const onSubmit = (boardData) => {
        axios(`/api/glanner-board`,
            {method: 'POST', data: {glannerId: Number(id), files: [], title: boardData.boardTitle, content: boardData.boardContent}})
            .then(res => {
                // console.log('작성 완료!')                
                refreshData(page)
            })
            .catch(err => console.log(err))
    }
    
    const onSubmitComment = (content, boardId) => {
        if (content.length === 0) {
            alert('댓글을 작성해주세요.')
            return
        }
        axios(`/api/glanner-board/comment`, {method: 'POST', data: {boardId, content, parentId: null}})
            .then(res => {
                refreshData(page)
            })
            .catch(err => console.log(err))
    }

    // 실시간 렌더링 용도(무한 스크롤 리스트 페이지에서도 게시글, 댓글 수정, 삭제가 실시간으로 반영!!)
    const refreshData = (nowPage) => {
        axios(`/api/glanner-board/${id}/0/${(nowPage + 1) * 5}`)
            .then(res => {
                setBoardList(res.data)
                reset()
            })
            .catch(err => console.log(err))
    }
    return <GlannerBoardPresenter 
                boardList={boardList}
                register={register}
                errors={errors}
                handleSubmit={handleSubmit}
                onSubmit={onSubmit}
                // onSubmitComment={onSubmitComment}
                refreshData={refreshData}                
                loading={loading}
                InfiniteScroll={InfiniteScroll}
                page={page}
            />
} 