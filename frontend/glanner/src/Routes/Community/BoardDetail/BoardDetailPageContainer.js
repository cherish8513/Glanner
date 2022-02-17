import axios from "axios";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useParams } from "react-router-dom";
import { BoardDetailPagePresenter } from "./BoardDetailPagePresenter";

export const BoardDetailPageContainer = () => {
  const { id } = useParams();
  const [loading, isLoading] = useState(true);
  const [post, setPost] = useState({});
  const [postLikeCount, setPostLikeCount] = useState(0);
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState("");
  const { pathname } = useLocation();

  const [glannerInfo, setGlannerInfo] = useState({});
  
  useEffect(() => {    
    getBoard()
    // if (pathname.includes('/notice/')) {
    //   axios(`/api/notice/${id}`, {method: 'GET'})
    //  .then(res => {
    //    setPost(() => res.data)    
    //    isLoading(false)
    //   })
    //  .catch(err => console.log(err))   
    // } else if (pathname.includes('/free/')) {     
      
    //   // 게시글 데이터 가져오기
    //   axios(`/api/free-board/${id}`)
    //     .then(res => {
    //       setPost(res.data)          
    //       setComments(res.data.comments)
    //       setPostLikeCount(res.data.likeCount)
    //       isLoading(false)
    //     })
    //     .catch(err => console.log(err))

    // } else if (pathname.includes('/group/')) {
    //   axios(`/api/group-board/${id}`, {method: 'GET'})
    //    .then(res => { 
    //       setPost(res.data)
    //       fetchGlannerInfo()          
    //       setComments(res.data.comments)
    //       setPostLikeCount(res.data.likeCount)    
    //       isLoading(false)
    //     })
    //    .catch(err => console.log(err))    
    // }
  }, [id, pathname])
  
  // 게시글 데이터 가져오기
  const getBoard = () => {
    if (pathname.includes('/free/')) {
      axios(`/api/free-board/${id}`, {method: 'GET'})
        .then(res => {
          setPost(() => res.data)
          setComments(res.data.comments.reverse())
          setPostLikeCount(res.data.likeCount)    
          isLoading(false)
        })
        .catch(err => console.log(err)) 
    } else if (pathname.includes('/group/')) {
      axios(`/api/group-board/${id}`, {method: 'GET'})
        .then(res => {
          // console.log(res.data)
          setPost(() => res.data)
          setComments(res.data.comments.reverse())
             
          isLoading(false)
      })
      fetchGlannerInfo()
    } else if (pathname.includes('/notice/')) {
      axios(`/api/notice/${id}`, {method: 'GET'})
     .then(res => {
       setPost(() => res.data)    
       isLoading(false)
      })
     .catch(err => console.log(err))   
    }
  }
  
  // 해당 게시글의 좋아요 + 1
  const addLike = () => {
    // if (pathname.includes('/notice/')) {
    //   axios(`boardlist/${id}`, {
    //   method: 'PATCH',
    //   data: {like: post.like + 1}
    // })
    //   .then(res => {
    //     setPost(res.data)
    //   })
    //   .catch(err => console.log(err))
    // } else 
    if (pathname.includes('/free/')) {
      axios(`/api/free-board/like/${id}`, {
      method: 'PUT',
      // data: {like: post.like + 1}
    })
      .then(res => {
        setPostLikeCount(postLikeCount + 1)
      })
        .then((res) => {
          setPost(res.data);
        })
        .catch((err) => console.log(err));
    } else if (pathname.includes("/group/")) {
      axios(`groupBoardList/${id}`, {
        method: "PATCH",
        data: { like: post.like + 1 },
      })
        .then((res) => {
          setPost(res.data);
        })
        .catch((err) => console.log(err));
    }
  };

  //  해당 게시글에 댓글 && 대댓글 남기기
  // responseTo는 부모 댓글의 id임. responseTo가 -1 이라면 루트 댓글, responseTo가 -1이 아니라 자연수라면 대댓글임
  const addComment = (newCommentData, parentCommentId) => {
    const today = Date();
    const commentData = {
      content: newCommentData,
      boardId: post.boardId,
      parentId: parentCommentId
    }
    if (pathname.includes('/free/')) {
      axios(`/api/free-board/comment`, {
        method: 'POST',
        data: commentData
      }).then(res => {        
        // setComments(comments.concat(res.data))
        getBoard() 
      })
        .catch(err => console.log(err.message))
    } else if (pathname.includes('/group/')) {
      axios(`/api/group-board/comment`, {
        method: 'POST',
        data: commentData
      }).then(res => {
        // setComments(comments.concat(res.data)) 
        getBoard()
      })
        .then((res) => setComments(comments.concat(res.data)))
        .catch((err) => console.log(err.message));
    }
  };
  // 댓글 && 대댓글 수정하기
  const updateComment = (commentContent, commentData) => {    
    if (pathname.includes('/free/')) {
      axios(`/api/free-board/comment/${commentData.commentId}`, {
        method: 'PUT',
        data: {content: commentContent}
      }).then(res => {
        getBoard()
      } 
        )
        .catch(err => console.log(err.message))
    } else if (pathname.includes('/group/')) {
      axios(`/api/group-board/comment/${commentData.commentId}`, {
        method: 'PUT',
        data: {content: commentContent}
      }).then(res => {
        getBoard()
      } 
        )
        .catch(err => console.log(err.message))
    }
    
  };

  // 댓글 && 대댓글 좋아요 + 1
  const addCommentLike = (_comment) => {
    if (pathname.includes("/free/")) {
      axios(`comments/${_comment.id}`, {
        method: "PATCH",
        data: { like: _comment.like + 1 },
      })
        .then((res) => {
          const newCommentsData = comments.map((comment) => {
            if (comment.id === _comment.id) {
              comment.like++;
              return comment;
            } else {
              return comment;
            }
          });
          setComments(newCommentsData);
        })
        .catch((err) => console.log(err));
    } else if (pathname.includes("/group/")) {
      axios(`groupComments/${_comment.id}`, {
        method: "PATCH",
        data: { like: _comment.like + 1 },
      })
        .then((res) => {
          const newCommentsData = comments.map((comment) => {
            if (comment.id === _comment.id) {
              comment.like++;
              return comment;
            } else {
              return comment;
            }
          });
          setComments(newCommentsData);
        })
        .catch((err) => console.log(err));
    }
    
  }
  
  // glanner 정보 가져오기
  const fetchGlannerInfo = () => {
    axios(`/api/group-board/glanner/${id}`)
      .then(res => {
        setGlannerInfo(res.data)
      })
      .catch(err => console.log(err))
  }
  // 글래너에 멤버 추가하기
  const addMember = (userEmail) => {
    axios(`/api/group-board/glanner/${id}`)
      .then(res => {
        axios(`/api/glanner/user`, {method: 'POST', data: {email: userEmail, glannerId: res.data.glannerId}})
          .then(res => {
            alert('글래너에 추가되었습니다.')
            // setGlannerInfo(glannerInfo.numOfMember + 1)
            fetchGlannerInfo()
            getBoard()
          })
          .catch(err => {
            alert('글래너에 추가할 수 없습니다.')
          })
      })
      .catch(err => console.log(err))    
  }
  return (
    <BoardDetailPagePresenter
      loading={loading}
      post={post}
      addLike={addLike}
      content={content}
      setContent={setContent}
      comments={comments}
      setComments={setComments}
      addComment={addComment}
      addCommentLike={addCommentLike}
      updateComment={updateComment}
      pathname={pathname}
      postLikeCount={postLikeCount}
      glannerInfo={glannerInfo}
      addMember={addMember}
    />
  );
};
