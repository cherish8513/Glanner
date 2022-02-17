import React, { useEffect, useState } from "react";
import BoardPagePresenter from "./BoardPagePresenter";
import Helmet from "react-helmet";
import axios from "axios";
import { useLocation } from "react-router-dom";
// import { useParams } from "react-router-dom";

export default function BoardPageContainer () { 
    // const type = useParams();   
    const [loading, setLoading] = useState(true);
    const [boardList, setBoardList] = useState([]);
    const [latestNoticeList, setLatestNoticeList] = useState([]);
    const [inputData, setInputData] = useState("");
    const [page, setPage] = useState(0);
    // const [totalPage, setTotalPage] = useState(1);

    const fetchBoardList = () => {
      axios(`/api/free-board/${page}/6`, {method: 'GET'})
        .then(res => {
          setBoardList(res.data)
          // console.log(res.data)
          setLoading(false)
        })
        .catch(err => console.log(err))
    }

    const fetchLatestNoticeList = () => {
      axios(`/api/notice/0/3`, {method: 'GET'})
      .then(res => {
        setLatestNoticeList(res.data) 
        setLoading(false)
      })
      .catch(err => console.log(err))
    }
    useEffect(() => {
      fetchBoardList()
      fetchLatestNoticeList()
    }, [page])

    const handleChangePage = newPage => {
      setPage((newPage - 1) * 9)     
    }
    const handleInput = (e) => {
      // console.log(e.target.value)
      setInputData(e.target.value)
    }
    const searchBoard = () => {
      
      axios(`/api/free-board/search/${page}/9/?keyword=${inputData}`, { method: 'GET' })
        .then(res => {
            // setInputData('')
            setBoardList(res.data)
            setLoading(false)
          }
        )
        .catch(err => console.log(err))
    }
    
    return (
      <>
        <Helmet>
          <title>Glanner | 게시판</title>
        </Helmet>
        <BoardPagePresenter
          loading={loading}
          boardList={boardList}
          latestNoticeList={latestNoticeList}
          handleChangePage={handleChangePage}
          inputData={inputData}
          handleInput={handleInput}
          searchBoard={searchBoard}
          // type = {type}
        />
      </>
    );
  }
