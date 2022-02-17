import { Link } from "react-router-dom";

import styled from "styled-components";

import {
  Box,
  List,
  Grid,
  ListItemText,
  Divider,
  Paper,
  ListItem,
} from "@mui/material";

const AlarmPageDiv = styled.div`
  font-size: 20px;
  span {
    font-family: "Noto Sans KR" !important;
  }
`;

const confirmedStyle = {
  color: "#B4B4B4",
};
const unConfirmedStyle = {
  color: "#000000",
  fontWeight: "bold",
};

export default function AlarmPagePresenter({ alarmList, loading }) {
  console.log(alarmList);

  return (
    <AlarmPageDiv>
      <Box sx={{ textAlign: "center", minHeight: "100%" }}>
        <List
          sx={{
            width: "90%",
            bgcolor: "background.paper",
            margin: "0 auto",
            minHeight: "100%",
          }}
        >
          <Grid container>
            <Grid item xs={1}>
              <ListItemText primary={"알람"} />
            </Grid>
            <Grid item xs={1.8}>
              <ListItemText>종류</ListItemText>
            </Grid>
            <Grid item xs={7}>
              <ListItemText>내용</ListItemText>
            </Grid>
            <Grid item xs={1}>
              <ListItemText>작성일</ListItemText>
            </Grid>
          </Grid>
          <Divider />
          {loading ? (
            <div style={{ marginTop: "20px" }}>Loading</div>
          ) : (
            <div width="100%" height="100%">
              {alarmList != null
                ? alarmList.map(
                    ({ type, typeId, confirmation, content, createdDate }) => {
                      return (
                        <Paper
                          sx={{ my: "5px" }}
                          style={
                            confirmation === "STILL_NOT_CONFIRMED"
                              ? unConfirmedStyle
                              : confirmedStyle
                          }
                        >
                          <Link
                            to={
                              type === "BOARD"
                                ? `/board/group/${typeId}`
                                : `/conference/${typeId}`
                            }
                          >
                            <ListItem>
                              <Grid container style={{ width: "100%" }}>
                                <Grid item xs={1}>
                                  <ListItemText
                                    sx={{
                                      whiteSpace: "nowrap",
                                      overflow: "hidden",
                                    }}
                                  >
                                    {type === "BOARD" ? "게시판" : "하루일과"}
                                  </ListItemText>
                                </Grid>
                                <Grid item xs={9}>
                                  <ListItemText>{content}</ListItemText>
                                </Grid>
                                <Grid item xs={2}>
                                  <ListItemText>
                                    {String(createdDate).split("T")[0]}{" "}
                                    {String(createdDate)
                                      .split("T")[1]
                                      .substring(0, 8)}
                                  </ListItemText>
                                </Grid>
                              </Grid>
                            </ListItem>
                          </Link>
                        </Paper>
                      );
                    }
                  )
                : ""}
            </div>
          )}
        </List>
      </Box>
    </AlarmPageDiv>
  );
}
