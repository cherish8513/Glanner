import React, { useEffect } from 'react';
import { styled, alpha } from '@mui/material/styles';
import { Menu, MenuItem, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import DeleteIcon from '@mui/icons-material/Delete';
import FlagIcon from '@mui/icons-material/Flag';
import { MoreVert } from '@material-ui/icons';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import axios from 'axios';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import jwt_decode from "jwt-decode";


const StyledMenu = styled((props) => (
  <Menu
    elevation={0}
    anchorOrigin={{
      vertical: 'bottom',
      horizontal: 'right',
    }}
    transformOrigin={{
      vertical: 'top',
      horizontal: 'right',
    }}
    {...props}
  />
))(({ theme }) => ({
  '& .MuiPaper-root': {    
    borderRadius: 6,
    marginTop: theme.spacing(1),
    minWidth: 180,
    color:
      theme.palette.mode === 'light' ? 'rgb(55, 65, 81)' : theme.palette.grey[300],
    boxShadow:
      'rgb(255, 255, 255) 0px 0px 0px 0px, rgba(0, 0, 0, 0.05) 0px 0px 0px 1px, rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px',
    '& .MuiMenu-list': {
      padding: '4px 0',
    },
    '& .MuiMenuItem-root': {
      '& .MuiSvgIcon-root': {
        fontSize: 18,
        color: theme.palette.text.secondary,
        marginRight: theme.spacing(1.5),
      },
      '&:active': {
        backgroundColor: alpha(
          theme.palette.primary.main,
          theme.palette.action.selectedOpacity,
        ),
      },
    },
  },
}));



export default function GlannerMoreBtn({ editData, type, refreshData, page, comment, setComments, getGlannerComments, setCommentCount }) {
  const navigate = useNavigate();
  const { pathname } = useLocation();  
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [path, setPath] = React.useState("");
  const { id } = useParams();
  const [added, setAdded] = React.useState(true);
  const [authData, setAuthData] = React.useState({});
  const [hostMail, setHostMail] = React.useState('');
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
    // setOpen(false)
  };

  useEffect(() => {
    // if (pathname.includes('/notice/')) {
    //   setPath('latestNoticeList')
    // } else if (pathname.includes('/free/')) {
    //   setPath('free-board')
    // } else if (pathname.includes('/group/')) {
    //   setPath('group-board')
    // }
    const token = localStorage.getItem('token');
    const decoded = jwt_decode(token);
    setAuthData(decoded)
  }, [pathname])

// 게시글 && 댓글 삭제
  const deleteItem = (item, type) => {
    // console.log(type)
    const ok = window.confirm('삭제하겠습니까?')
    if (ok) {
      // 게시글인 경우
      if (type === 'body') {
        axios({
          url: `/api/glanner-board/${item.boardId}`,
          method: 'DELETE'})
          .then(res => {        
            alert('삭제되었습니다.')
            refreshData(page)
          })
          .catch(err => {
            alert('삭제할 수 없습니다.')
          })
    //   // 댓글인 경우
      } else if (type === 'comment') {        
        axios({
        url: `/api/glanner-board/comment/${item.commentId}`,
        method: 'DELETE'})
        .then(res => {        
            // alert('삭제되었습니다.')
            getGlannerComments()
            setCommentCount(prev => prev - 1)
        })
        .catch(err => {
            alert('삭제할 수 없습니다.')
        })        
      }
    }    
  }
// // 게시글 수정
//   const updateItem = (item, type) => {    
//     if (type === '/free/body') {
//       navigate(`/board-form`, {state: editData})
//     } else if (type === '/group/body') {
//       navigate(`/group-form`, {state: editData})
//     } else if (type === '/notice/body') {
//       navigate(`/notice-form`, {state: editData})
//     } else {
//       setOpenForm(true);
//       if (setUpdateFlag) setUpdateFlag(true);
//       setContent(item.content);
//     }
//   }
  
//   useEffect(() => {
//     // glanner에 포함된 유저인지 확인 용도
//     axios(`/api/group-board/glanner/${id}`)
//       .then(res => { 
//         setHostMail(res.data.hostEmail)       
//         res.data.membersInfos.map(info => {
//           if (info.userEmail === editData.userEmail) {
//             setAdded(false)
//             return
//           }
//         })
//       })
//       .catch(err => console.log(err))
//   }, [])
  return (
    <div style={{ display: 'inline'}}>
      <IconButton
        id="demo-customized-button"
        aria-controls={open ? 'demo-customized-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        variant="contained"
        disableelevation="true"
        onClick={handleClick}
        endicon={<KeyboardArrowDownIcon />}
      >
        <MoreVert />
      </IconButton>
      <StyledMenu
        
        id="demo-customized-menu"
        MenuListProps={{
          'aria-labelledby': 'demo-customized-button',
        }}
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
      >          
        {authData.sub === editData.userEmail &&
        <div>
          {/* <MenuItem onClick={() => {}
            // updateItem(editData, type, comments, setComments)
            } disableRipple>
            <EditIcon />
            수정
          </MenuItem>         */}
          <MenuItem onClick={() => deleteItem(editData, type)} disableRipple>
            <DeleteIcon />
            삭제
          </MenuItem>
        </div>}
        <MenuItem onClick={handleClose} disableRipple>
          <FlagIcon />
          신고
        </MenuItem>        
      </StyledMenu>
    </div>
  );
}