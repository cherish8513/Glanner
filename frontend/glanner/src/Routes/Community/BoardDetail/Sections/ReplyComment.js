import { Divider } from "@mui/material"
import { makeStyles } from "@mui/styles";
import { boardStyles } from "../../Board.styles";
import { SingleComment } from "./SingleComment";

const useStyles = makeStyles(boardStyles);

export const ReplyComment = ({commentList, parentCommentId, addCommentLike, setComments, updateComment, glannerInfo, addMember}) => {
  const classes = useStyles();

  return (
    // 대댓글 박스
    <>
      {commentList && commentList.map(comment => {
        if (comment.parentId === parentCommentId) {
          return (
            <SingleComment
              comments={commentList}            
              comment={comment}
              // addComment={addComment}
              addCommentLike={addCommentLike}
              setComments={setComments}
              updateComment={updateComment}
              key={comment.commentId}
              glannerInfo={glannerInfo}
              addMember={addMember}
            />            
          )
        }
      })}
      <Divider className={classes.comments} />
    </>
  )
}