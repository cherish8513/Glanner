import { Avatar, CardActions, CardContent, CardHeader, Chip, Divider, IconButton, Stack } from "@mui/material"
import { grey } from "@mui/material/colors"
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import { makeStyles } from "@mui/styles";
import { boardStyles } from "../../Board.styles";
import MoreBtn from "../../../../Components/MoreBtn";
import moment from "moment";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { GroupMember } from "./GroupButton";
import axios from "axios";
import { ReactComponent as CircleUser } from "../../../../assets/circle-user-solid.svg";

const useStyles = makeStyles(boardStyles);

export const DetailBody = ({ post, addLike, postLikeCount, glannerInfo }) => {
  const classes = useStyles();
  const { pathname } = useLocation();
  const [groupPage, setGroupPage] = useState(false);
  const [noticePage, setNoticePage] = useState(false);
  useEffect(() => {
    if (pathname.includes('/group/')) {
      setGroupPage(true)
    }
    if (pathname.includes('/notice/')) {
      setNoticePage(true)
    }
  }, [pathname])
  return (
    <>    
      <CardHeader
        avatar={
          // <Avatar sx={{ bgcolor: grey[500] }} aria-label="recipe">              
          // </Avatar>
          <CircleUser
            style={{
              fontSize: 40 + "px",
              color: "#5F5F5F",
              backgroundColor: "#F2D0D9",
              borderRadius: "50%",
              marginRight: 2
            }}
          /> 
        }
        action={
          groupPage ?
          <div style={{display: 'flex', justifyContent: 'space-between', width: '170px'}}>
              <GroupMember post={post} glannerInfo={glannerInfo} />
              <MoreBtn editData={post} type={`/group/body`} />
          </div>
          :
          <MoreBtn editData={post} type={`/free/body`} glannerInfo={glannerInfo} />
        }
        
        title={post.userName}
        subheader={moment(post.createdDate).format('YYYY.MM.DD HH:mm:ss')}
        className={classes.dateText}
      />
      <Divider />
      {/* {post.attachment && <CardMedia
        component="img"
        height="194"
        image={post.attachment}
        alt="image"
      />} */}
      <CardContent>
        <h1 className={classes.title}>
          {post.title}
        </h1>
        <p className={classes.content}>
          {post.content}
        </p>
      </CardContent>
      {groupPage &&
      <Stack spacing={1} sx={{mt: 5}}>        
        <Stack direction="row" spacing={1}>          
          {post.interests && post.interests.split('#').slice(1).map((title, idx) => {
            return <Chip label={title} size="small" sx={{borderRadius: '5px', color: 'white', backgroundColor: "#8C7B80"}} key={idx} />
          })}
        </Stack>
      </Stack>}
      <CardActions disableSpacing sx={{display: 'flex', justifyContent: 'space-between'}}>
        <span className={classes.botText}>조회수 {post.count}{postLikeCount ? <>{`, 좋아요 ${postLikeCount}`}</>: null } </span>
        
        <div style={{height: 40}}>
          {!groupPage && !noticePage &&
            <>
              <IconButton onClick={addLike}>
                <FavoriteIcon />
              </IconButton>
              <IconButton aria-label="share">
                <ShareIcon />
              </IconButton>
            </>
          }
        </div>       
      </CardActions>
    </>
  )
}