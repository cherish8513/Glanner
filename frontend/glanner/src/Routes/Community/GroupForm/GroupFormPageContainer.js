import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { GroupFormPagePresenter } from "./GroupFormPagePresenter"

export const GroupFormPageContainer = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const [data, setData] = useState({
    title: "",
    content: "",
    files: [],
    interests: "",
    // date: "",
    // writer: "테스트 유저",
    // count: 0,
    // like: 0,
    // comments: [],
    // attachment: "",
    // tags: [],
    // present: 1,
    numOfPeople: 5,    
  })
  const [tagItems, setTagItems] = useState([]);
  const [titleError, setTitleError] = useState(false);
  const [contentError, setContentError] = useState(false);
  const [attachment, setAttachment] = useState("");

  // 글 수정인 경우, 기존 데이터를 입력창에 채워주기
  useEffect(() => {
    if (state) {
      setData(state)
      setTagItems(state.interests.split('#').slice(1))
    }
  }, [state])

  const handleSubmit = (e) => {
    e.preventDefault();

    setTitleError(false)
    setContentError(false)
    if (data.title === '') {
      setTitleError(true)
      alert('제목을 입력해주세요.')
      return
    }

    if (data.content === '') {
      setContentError(true)
      alert('내용을 입력해주세요.')
      return
    }
    if (tagItems.length === 0) {
      alert('관심사를 입력해주세요.')
      return
    }
    // console.log(tagItems)    
    // 기존 글을 수정하는 로직
    if (state) {
      let tagString = ''
      tagItems.map(item => {
        tagString += `#${item}`
      })
      axios({
        url: `/api/group-board/${state.boardId}`,
        method: 'PUT',
        data: {
          title: data.title,
          content: data.content,
          files: [],
          interests: tagString
          // date: today,      
          // writer: data.writer,
          // count: data.count,
          // like: data.like,
          // comments: data.comments,
          // attachment: data.attachment,
          // tags: tagItems,
          // present: data.present,
          // full: data.full,
        }
      })
        .then(res => {
          alert('수정 성공!')
          // console.log(res.data)
          // navigate(`/board/group/${res.data.boardId}`)
          navigate(`/community/group`)
        })
        .catch(err => alert('수정 실패!'))    
    
    } else {
      // 새로운 글 작성하는 로직
      let tagString = ''
      tagItems.map(item => {
        tagString += `#${item}`
      })
      axios({
        url: '/api/group-board',
        method: 'POST',
        data: {
          title: data.title,
          content: data.content,
          files: [],
          interests: tagString
          // date: today,      
          // writer: data.writer,
          // count: data.count,
          // like: data.like,
          // comments: data.comments,
          // attachment: data.attachment,
          // interests: tagItems,
          // present: data.present,
          // full: data.full,
        }
      })
        .then(res => {
          alert('작성 성공!')
          axios(`/api/glanner/`, {method: 'PUT', data: {glannerId: res.data.glannerId, glannerName: data.title}})
            .then(res => window.location.reload())
            .catch(err => console.log(err))
          // navigate(`/board/group/${res.data.id}`)
          navigate(`/community/group`)
        })
        .catch(err => alert('작성 실패!'))
    }
  };
   // 첨부한 이미지 파일을 string으로 저장하기, 
   const onFileChange = (event) => {
    const {target: {files}} = event;
    const theFile = files[0];        // input 태그의 file은 한개
    const reader = new FileReader(); // FileReader 객체는 파일(이미지 등)의 내용을 읽어서 문자열로 저장해줌.
    
    reader.onloadend = (finishedEvent) => {  // 읽기 동작이 끝났을 때 onloadend 이벤트 발생
      const {currentTarget: {result}} = finishedEvent;
      setAttachment(result);
    };
    if (theFile) {
      reader.readAsDataURL(theFile);
    };    
  };

  const handle = (e) => {
    console.log(e.target.id ? e.target.id : e.target.name)
    const newData = {...data}
    newData[e.target.id ? e.target.id : e.target.name] = e.target.value
    setData(newData)
  };
  function handleSelecetedTags(items) {
    setTagItems(items)
            
  }  
  const deleteFile = () => {
    setAttachment("")
  }
  return (
    <GroupFormPagePresenter 
      handleSubmit={handleSubmit}
      handle={handle}
      onFileChange={onFileChange}
      state={state}
      data={data}
      attachment={attachment}
      deleteFile={deleteFile}
      handleSelecetedTags={handleSelecetedTags}
      state={state}
    />
  )
}