import React, { useEffect, useState } from "react";
import FindGroupPresenter from "./FindGroupPresenter";
import Helmet from "react-helmet";
import axios from "axios";

export default function FindGroupContainer () { 

  const [loading, setLoading] = useState(true);
  const [groupBoardList, setGroupBoardList] = useState([]);
  const [page, setPage] = useState(0);
  const [category, setCategory] = useState('search');
  const [inputData, setInputData] = useState("");
    useEffect(() => {      
      fetchGroupBoardList()
    }, [page])
    const fetchGroupBoardList = () => {
      axios(`/api/group-board/${page}/6`, { method: 'GET' })
      .then(res => {
        setGroupBoardList(res.data)
        setLoading(false)
      }
      )
      .catch(err => console.log(err))
    }
    const handleChangePage = newPage => {
      setPage((newPage - 1) * 6)
    }
    const handleInput = (e) => {
      // console.log(e.target.value)
      setInputData(e.target.value)
    }

    // 검색 기능
    const searchBoard = () => {
      if (category === 'search') {
        axios(`/api/group-board/search/${page}/4/?keyword=${inputData}`, { method: 'GET' })
          .then(res => {
              setInputData('')
              setGroupBoardList(res.data)
              setLoading(false)
            }
          )
        .catch(err => console.log(err))
      } else if (category === 'interest') {
        axios(`/api/group-board/interest/${page}/4/?keyword=${inputData}`, { method: 'GET' })
          .then(res => {
              setInputData('')
              setGroupBoardList(res.data)
              setLoading(false)
            }
          )
          .catch(err => console.log(err))
      }
    }
    return (
      <>
        <Helmet>
          <title>Glanner | 게시판</title>
        </Helmet>
        <FindGroupPresenter
          loading={loading}
          groupBoardList={groupBoardList}
          handleChangePage={handleChangePage}
          inputData={inputData}
          handleInput={handleInput}
          searchBoard={searchBoard}
          category={category}
          setCategory={setCategory}
        />
      </>
    );
  }

