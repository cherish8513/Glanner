import React from "react";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import InputAdornment from "@mui/material/InputAdornment";
import FormControl from "@mui/material/FormControl";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ReactComponent as CircleUser } from "../../../assets/circle-user-solid.svg";
// import { ReactComponent as UserFriend } from "../../../assets/user-friends.svg";
import "./FindGroup.css";
import {
  Button,
  Paper,
  Grid,
  Typography,
  Divider,
  Stack,
  Chip,
  Pagination,
  Select,
  MenuItem,
  FormGroup,
} from "@mui/material";
import PeopleIcon from '@mui/icons-material/People';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import { Link, useNavigate } from "react-router-dom";
import { makeStyles } from "@mui/styles";
import "moment/locale/ko";
import { boardStyles } from "../Board.styles";
import { getListTime } from "../helper";

const useStyles = makeStyles(boardStyles)

export default function FindGroupPresenter({ 
  type, 
  loading, 
  groupBoardList,
  handleChangePage,
  category,
  setCategory,
  handleInput,
  inputData,
  searchBoard
}) {
  const navigator = useNavigate();  
  const classes = useStyles();
  // console.log(groupBoardList)
  return (
    <>
      {loading && <div>Loading...</div>}      
      {groupBoardList &&
      <Box sx={{ mt: 2}}>        
        <Box
          component="form"
          noValidate
          autoComplete="off"
          sx={{ textAlign: "right", width: "95%", minHeight: "100%", m: 0 }}
        >
          
          <FormGroup sx={{ float: 'right', width: "25ch", mb: 1, mt: 3, height: 50,}} size="small">
            {/* <InputLabel htmlFor="search-board">이름, 제목</InputLabel> */}
            <OutlinedInput
              value={inputData}
              onChange={handleInput}
              onKeyUp={(e) => {
                if (e.keyCode === 13) {
                  searchBoard()
                }
              }}
              id="search-board"
              startAdornment={
                <Select
                  sx={{
                    "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                      border: "0px solid #484850",
                      borderRadius: "5px 5px 0 0"
                    },
                    // "fieldset": {border: 'none'},
                    "legend": {width: 0},
                    fontSize: 13,
                    width: 'auto',
                    color: '#959595',
                    right: 15
                  }}
                  value={category}
                  // sx={{fontSize: 5}}
                  onChange={e => setCategory(e.target.value)}
                >
                  <MenuItem sx={{fontSize: 13}} value={'search'}>제목 + 내용</MenuItem>
                  <MenuItem sx={{fontSize: 13}} value={'interest'}>관심사</MenuItem>
                </Select>
              }
              endAdornment={
                <InputAdornment position="end">
                  <IconButton aria-label="search-button" edge="end">
                    <FontAwesomeIcon icon={faSearch} className="searchIcon" />
                  </IconButton>
                </InputAdornment>
              }
              label="Password"
              sx={{ borderRadius: 5, background: '#F7F6F6',
                border: '2px solid #E5E5E5', "legend": {width: 0},
                "fieldset": {border: 'none'}, height: 40,
              }}
            />
          </FormGroup>          
        </Box>
        <Divider sx={{mb: 1, width: '88%', mx: 'auto'}} />
        <Box className={classes.textField} sx={{ textAlign: "left", height: 'auto', px: 15 }}>
          <Grid container rowSpacing={1.2} columnSpacing={{ xs: 1.5 }}>
          {groupBoardList && groupBoardList.map(({ boardId, title, userName, createdDate, content, userCount, interests, count, commentCount }) => (
            <Grid item xs={6} key={boardId} >
              <Paper sx={{ px: 3, py: 2, height: 'auto' }}>
                  <Grid container direction="column" spacing={0} sx={{height: 'auto'}}>
                      <Grid item onClick={() => navigator(`/board/group/${boardId}`)} sx={{cursor: 'pointer'}}>
                        <Grid container sx={{justifyContent: 'space-between'}}>
                          <Typography className='title' gutterBottom variant="h5" sx={{color: '#262626'}}>                          
                            {title}                            
                          </Typography>
                          <Typography sx={{color: '#959595', fontSize: 15 }}>
                            {getListTime(createdDate)}
                          </Typography>  
                        </Grid>
                        <Typography className = 'membernum' variant="body2">
                        {/* <UserFriend
                          style={{
                            color: "#5F5F5F",
                          }}
                        />  */}
                          <PeopleIcon fontSize="small" sx={{ mb: '3px'}} />
                          <Box component='span' sx={{ ml: 1, fontSize: 15}}>
                            {userCount} / 5
                          </Box>
                        </Typography>
                        <Typography sx={{color: '#5F5F5F', fontWeight: 800, display: '-webkit-box', overflow: 'hidden', WebkitBoxOrient: 'vertical', WebkitLineClamp: 3, my: 1, height: '20px' }} className='boardpost' variant="body2" color="text.secondary">
                          {content}
                        </Typography>

                        {/* <Typography className='boardpost' variant="body2" color="text.secondary">
                          
                        </Typography> */}
                      </Grid>
                    <Grid item style={{paddingTop: 5}}>   
                      <Stack spacing={1}>        
                        <Stack direction="row" spacing={0.5}>
                          {interests && interests.split('#').slice(1).map((tag, idx) => {
                            return <Chip label={tag} size="small" sx={{borderRadius: '5px', color: 'white', backgroundColor: "#8C7B80"}} key={idx} />                            
                          })}
                        </Stack>
                      </Stack>
                    </Grid>
                    <Grid style={{paddingTop: 8}} item container sx={{justifyContent: 'space-between'}}>
                      <Box>
                        <CircleUser
                          style={{
                            fontSize: 15 + "px",
                            color: "#5F5F5F",
                            backgroundColor: "#F2D0D9",
                            borderRadius: "50%",
                            marginRight: 2
                          }}
                          /> 
                        <Typography className='nickName' sx={{ fontSize: 14, cursor: 'pointer', mx: 0.5 }} component='span'>
                          {userName}
                        </Typography>
                      </Box>
                      <Box sx={{color: '#808080'}}>
                        <Typography component='span' sx={{mx: 1}}>
                          <VisibilityOutlinedIcon sx={{transform: 'scale(0.6)'}} />
                          <span style={{ fontSize: '14px'}}>{count}</span>
                        </Typography>
                        <Typography component='span'>

                          <ChatBubbleOutlineOutlinedIcon sx={{transform: 'scale(0.6)'}} />
                          <span style={{ fontSize: '14px'}}>{commentCount}</span>
                        </Typography>                
                      </Box>                      
                    </Grid>
                  </Grid>

                </Paper>
            </Grid>
            ))}
            {!loading && groupBoardList.length === 0 && <div>검색 결과가 없습니다.</div>}          
          </Grid>
              
        
        </Box>
        <Divider sx={{my: 1, width: '90%', mx: 'auto'}} />
        <div style={{ width: "95%", textAlign: "right", marginTop: "12px" }}>
          <Link to={`/group-form`}>
            <Button 
              sx={{
                width: '90px',
                height: '35px',
                border: "1px solid #8C7B80",
                fontFamily: 'Noto Sans CJK KR',
                fontSize: '16px',
                fontWeight: 'medium',
                color: "#8C7B80",
                borderRadius: "10px",
                "&:hover": {
                  color: "#FFFFFF",
                  backgroundColor: "#8C7B80",
                  borderColor: "#8C7B80",
                  boxShadow: "none",
                }
              }}
            >글쓰기</Button>
          </Link>
          <Stack alignItems={'center'} spacing={2} sx={{mb: 10}}>
            <Pagination onChange={e => {handleChangePage(e.target.innerText)}} count={5} size="large"/>      
          </Stack>
        </div>
      </Box>}
    </>
  );
}
