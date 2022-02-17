import React from "react";
import styled from "styled-components";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import InputAdornment from "@mui/material/InputAdornment";
import FormControl from "@mui/material/FormControl";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Button,
  Paper,
  ListItemText,
  ListItem,
  List,
  Grid,
  Divider,
  Stack,
  Pagination,
  FormGroup,
} from "@mui/material";
import { Link } from "react-router-dom";
import { makeStyles } from "@mui/styles";
import "moment/locale/ko";
import { boardStyles } from "../Board.styles";
import { getListTime } from "../helper";

const useStyles = makeStyles(boardStyles);

const BoardListDiv = styled.div`
  span {
    font-family: 'Noto Sans KR' !important;
  }
`;

export default function BoardPagePresenter({
  type,
  loading,
  boardList,
  latestNoticeList,
  handleChangePage,
  inputData,
  handleInput,
  searchBoard
}) {
  const classes = useStyles();
  return (
    <BoardListDiv>
      {loading && <div>Loading...</div>}
      {(boardList || latestNoticeList) && (
        <>
          <Box
            component="form"
            noValidate
            autoComplete="off"
            sx={{ textAlign: "right", width: "95%", minHeight: "100%", m: 0, }}
          >
            <FormGroup
              sx={{ ml: 100, width: "25ch", mb: 1, mt: 3, height: 50 }}
              size="small"
            >
              {/* <InputLabel htmlFor="search-board">이름, 제목</InputLabel> */}
              <OutlinedInput
                value={inputData}
                onChange={handleInput}
                id="search-board"
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton aria-label="search-button" edge="end">
                      <FontAwesomeIcon icon={faSearch} className="searchIcon" />
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
                sx={{
                  borderRadius: 5,
                  background: "#F7F6F6",
                  border: "2px solid #E5E5E5",
                  legend: { width: 0 },
                  fieldset: { border: "none" },
                  height: 40,
                }}
              />
            </FormGroup>
          </Box>
          <Box
            className={classes.textField}
            sx={{ textAlign: "center", minHeight: "100%" }}
          >
            <List
              sx={{
                width: "90%",
                bgcolor: "background.paper",
                margin: "0 auto",
                minHeight: "100%",
                borderTop: "2px solid #e5e5e5",
              }}
            >
              <Grid container>
                <Grid item xs={1}>
                  <ListItemText primary={"말머리"} />
                </Grid>
                <Grid item xs={7}>
                  <ListItemText style={{fontFamily: "Noto Sans KR",}}>제목</ListItemText>
                </Grid>
                <Grid item xs={1.5}>
                  <ListItemText>글쓴이</ListItemText>
                </Grid>
                <Grid item xs={1.3}>
                  <ListItemText>작성일</ListItemText>
                </Grid>
                <Grid item xs={0.6}>
                  <ListItemText>조회</ListItemText>
                </Grid>
                <Grid item xs={0.6}>
                  <ListItemText>추천</ListItemText>
                </Grid>
              </Grid>
              <Divider />
              {latestNoticeList.map(
                ({ id, title, userName, createdDate, count, like }) => (
                  <Paper
                    sx={{ mb: 1, width: "100%", backgroundColor: "#F9F9F9" }}
                    key={id}
                  >
                    <ListItem
                      disableGutters
                      sx={{
                        height: "35px",
                        textAlign: "center",
                        color: "#DB1111",
                      }}
                    >
                      <Grid container>
                        <Grid item xs={1}>
                          <ListItemText primary={"[공지]"} />
                        </Grid>
                        <Grid item xs={7}>
                          <Link to={`/board/notice/${id}`}>
                            <ListItemText
                              sx={{ whiteSpace: "nowrap", overflow: "hidden" }}
                            >
                              {title}
                            </ListItemText>
                          </Link>
                        </Grid>
                        <Grid item xs={1.5}>
                          <ListItemText>{userName}</ListItemText>
                        </Grid>
                        <Grid item xs={1.3}>
                          <ListItemText>
                            {getListTime(createdDate)}
                          </ListItemText>
                        </Grid>
                        <Grid item xs={0.6}>
                          <ListItemText>{count}</ListItemText>
                        </Grid>
                        <Grid item xs={0.6}>
                          <ListItemText>{like}</ListItemText>
                        </Grid>
                      </Grid>
                    </ListItem>
                  </Paper>
                )
              )}
            </List>
            <List
              sx={{
                width: "90%",
                bgcolor: "background.paper",
                margin: "0 auto",
                borderBottom: "2px solid #E5E5E5",
                minHeight: "100%",
              }}
            >
              {boardList.map(
                ({
                  boardId,
                  title,
                  userName,
                  createdDate,
                  count,
                  likeCount,
                }) => (
                  <Paper
                    sx={{ mb: 1, width: "100%", backgroundColor: "#F9F9F9" }}
                    key={boardId}
                  >
                    <ListItem
                      disableGutters
                      sx={{
                        height: "35px",
                        textAlign: "center",
                        "&:hover": {
                          color: "#FFFFFF",
                          backgroundColor: "#8C7B80",
                          borderColor: "#8C7B80",
                          boxShadow: "none",
                        },
                      }}
                    >
                      <Grid container>
                        <Grid item xs={1}>
                          <ListItemText
                            primary={type == "notice" ? "[공지]" : "[자유]"}
                          />
                        </Grid>
                        <Grid item xs={7}>
                          <Link to={`/board/free/${boardId}`}>
                            <ListItemText
                              sx={{
                                textAlign: "start",
                                "&:hover": { color: "#FFFFFF" },
                              }}
                            >
                              {title}
                            </ListItemText>
                          </Link>
                        </Grid>
                        <Grid item xs={1.5}>
                          <ListItemText sx={{ mx: 3 }}>{userName}</ListItemText>
                        </Grid>
                        <Grid item xs={1.3}>
                          <ListItemText>
                            {getListTime(createdDate)}
                          </ListItemText>
                        </Grid>
                        <Grid item xs={0.6}>
                          <ListItemText>{count}</ListItemText>
                        </Grid>
                        <Grid item xs={0.6}>
                          <ListItemText>{likeCount}</ListItemText>
                        </Grid>
                      </Grid>
                    </ListItem>
                  </Paper>
                )
              )}
            </List>
          </Box>
          <div style={{ width: "95%", textAlign: "right", margin: "12px" }}>
            <Link to={"/board-form/"}>
              <Button
                sx={{
                  width: "90px",
                  height: "35px",
                  border: "1px solid #8C7B80",
                  fontFamily: "Noto Sans CJK KR",
                  fontSize: "16px",
                  fontWeight: "medium",
                  color: "#8C7B80",
                  borderRadius: "10px",
                  "&:hover": {
                    color: "#FFFFFF",
                    backgroundColor: "#8C7B80",
                    borderColor: "#8C7B80",
                    boxShadow: "none",
                  },
                }}
              >
                글쓰기
              </Button>
            </Link>
          </div>

          <Stack alignItems={"center"} spacing={2}>
            <Pagination
              onChange={(e) => handleChangePage(e.target.innerText)}
              count={5}
              sx={{ position: "fixed", bottom: 25 }}
              size="large"
            />
          </Stack>
        </>
      )}
    </BoardListDiv>
  );
}
