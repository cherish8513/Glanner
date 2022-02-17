import { Avatar, Button, CardActions, CardContent, CardHeader, Divider } from "@mui/material"
import { Box } from "@mui/system"
import { makeStyles } from '@mui/styles';
import { grey } from "@mui/material/colors";
import { useState } from "react";
import { CommentForm } from "./CommentForm";
import { boardStyles } from "../../Board.styles";
import MoreBtn from "../../../../Components/MoreBtn";
import { getTime } from "../../helper";
import { ReactComponent as CircleUser } from "../../../../assets/circle-user-solid.svg";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { useLocation, useParams } from "react-router-dom";
const useStyles = makeStyles(boardStyles)

export const SingleComment = ({
  boardId,
  comments, 
  setComments, 
  comment,
  addComment, 
  addCommentLike, 
  updateComment,
  pathname,
  glannerInfo,
  addMember,
}) => {
  const classes = useStyles();
  const [openForm, setOpenForm] = useState(false);
  const [content, setContent] = useState("");
  const [updateFlag, setUpdateFlag] = useState(false);
  const openCommentForm = () => {
    if (openForm) {
      setOpenForm(false)
    } else {
      setOpenForm(true)
    }
    setContent("")
    setUpdateFlag(false)  
  };
  const [added, setAdded] = useState(true);
  const { id } = useParams();
  // console.log('댓글')
  return (
    <Box className={comment.parentId === -1 ? classes.comments : classes.nestedComment} key={comment.boardId}>
      <CardHeader
        avatar={
          comment.parentId === -1 ? 
            <>
              <CircleUser
                style={{
                  fontSize: 30 + "px",
                  color: "#5F5F5F",
                  backgroundColor: "#F2D0D9",
                  borderRadius: "50%",
                  marginRight: 2
                }}
              />   
            </> 
            :
            <>
              <span 
                className={classes.replyIcon}
                style={{ position: 'relative', left: '-30px'}}
              >ㄴ</span>
              <CircleUser
                style={{
                  fontSize: 30 + "px",
                  color: "#5F5F5F",
                  backgroundColor: "#F2D0D9",
                  borderRadius: "50%",
                  marginRight: 2,
                  position: 'relative',
                  left: -25
                }}
              /> 
            </>
         }
        action={
          <>          
            <MoreBtn
              editData={comment}
              commentData={comment}
              commentUserName={comment.userName} 
              type={`${pathname}comment`}
              comments={comments} 
              setComments={setComments} 
              setOpenForm={setOpenForm} 
              setContent={setContent} 
              setUpdateFlag={setUpdateFlag}
              addMember={addMember}
              glannerInfo={glannerInfo}
              />
          </>
        }
        title={comment.userName}
        subheader={getTime(comment.createdDate)}
        className={classes.commentDateText}
        sx={ comment.parentId !== -1 ? {'& .MuiCardHeader-content': {padding: 0, position: 'relative', right: '25px'}} : null }
      />
      <CardContent sx={{padding: '0px 16px 0px 16px'}}>
        <p className={classes.commentContent} style={{ whiteSpace: 'normal', overflow: 'hidden', wordWrap: 'break-word'}}>
          {comment.content}
        </p>
      </CardContent>
      <CardActions disableSpacing sx={{display: 'flex', justifyContent: 'space-between'}}>
        <span>
          {comment.parentId === -1 && <Button className={classes.botText} onClick={openCommentForm} component="span">답글쓰기</Button>}
          <Button className={classes.botText} onClick={() => addCommentLike(comment)} component="span"> 좋아요 {comment.likeCount}</Button>
        </span>            
      </CardActions>      
      <Divider />
      
      {/* 댓글 or 대댓글 작성창 */}
      {openForm &&
        <CommentForm 
          comment={comment} 
          addComment={addComment} 
          setOpenForm={setOpenForm} 
          content={content} 
          setContent={setContent} 
          updateComment={updateComment}
          updateFlag={updateFlag}
          setUpdateFlag={setUpdateFlag}
        />
      }   
    </Box>    
  )
}