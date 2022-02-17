import React from 'react';
import { Typography, Divider } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { CommentForm } from './Sections/CommentForm';
import { DetailBody } from './Sections/DetailBody';
import { SingleComment } from './Sections/SingleComment';
import { ReplyComment } from './Sections/ReplyComment';
import { boardStyles } from '../Board.styles';
const useStyles = makeStyles(boardStyles)

export const BoardDetailPagePresenter = ({
  loading, 
  post,
  postLikeCount, 
  addLike, 
  content, 
  setContent, 
  comments, 
  setComments,
  addComment, 
  addCommentLike,
  updateComment,
  pathname,
  glannerInfo,
  addMember,
}) => {
  const classes = useStyles();  
  return (
    <>
    {loading && <div>Loading...</div>}
    {post && comments &&
      <div className={classes.card}>
        <DetailBody post={post} addLike={addLike} postLikeCount={postLikeCount} glannerInfo={glannerInfo} />

        <Divider />        
        <Typography component="div" sx={{ padding: '15px 24px 10px 24px', fontSize: '20px'}}> 댓글 {comments && comments.length}</Typography>            
        
        {/* 댓글 박스 */}       
        {comments && comments.map(comment => {           
            return (
            <div key={comment.commentId}>
              {comment.parentId === -1 &&
              <>
                <SingleComment
                  boardId={post.boardId}
                  comments={comments}            
                  comment={comment}
                  addComment={addComment}
                  addCommentLike={addCommentLike}
                  setComments={setComments}
                  updateComment={updateComment}
                  pathname={pathname}
                  glannerInfo={glannerInfo}
                  addMember={addMember}
                />
                <ReplyComment
                  addCommentLike={addCommentLike} 
                  commentList={comments} 
                  parentCommentId={comment.commentId} 
                  setComments={setComments}
                  updateComment={updateComment}
                  glannerInfo={glannerInfo}
                  addMember={addMember}
                />
              </>
              }
            </div>
        )})}
        {/* 댓글 작성창 */}
        <CommentForm 
          addComment={addComment} 
          content={content} 
          setContent={setContent}
          boardId={post.boardId}
          comments={comments}
          setComments={setComments}
        /> 
      </div>
      
    }
    
    </>
  );
}