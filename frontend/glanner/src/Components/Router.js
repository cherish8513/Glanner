import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import jwt_decode from "jwt-decode";

import Paper from "@mui/material/Paper";
import styled from "styled-components";

import MainPage from "../Routes/Planner/MainPlanner";
import Header from "./Header";
import Navigator from "./Navigator";
import BoardDetail from "../Routes/Community/BoardDetail";
import BoardList from "../Routes/Community/BoardList";
import BoardForm from "../Routes/Community/BoardForm";
import GroupForm from "../Routes/Community/GroupForm";
import NoticeList from "../Routes/Community/NoticeList";
import GroupBoardList from "../Routes/Community/GroupBoardList";
import FloatingActionButtons from "../Routes/Community/BoardDetail/Sections/FloatingActionButton";
import DailyPlanner from "../Routes/Planner/DailyPlanner/DailyPlanerContainer";
import GroupPlanner from "../Routes/Planner/GroupPlanner/GroupPlannerContainer";
import VideoConference from "../Routes/VideoConference/Video";
import GlannerBoard from "../Routes/Community/GlannerBoard/GlannerBoardContainer";
import AlarmPage from "../Routes/Alarm/AlarmPageContainer";

const HeaderDiv = styled.div`
  border-bottom: 2px solid #e5e5e5;
`;

export default function RouterComponent() {
  return (
    <>
      <Paper
        elevation={2}
        sx={{ width: "100%", minWidth: "900px", minHeight: "100%" }}
      >
        <Router>
          <HeaderDiv>
            <Header
              title={`${
                jwt_decode(localStorage.getItem("token")) != null
                  ? String(jwt_decode(localStorage.getItem("token")).sub).split(
                      "@"
                    )[0]
                  : ""
              }님의 플래너`}
            />
          </HeaderDiv>
          <Navigator
            PaperProps={{
              style: {
                height: 100 + "%",
                backgroundColor: "#f6f6f6",
                padding: "0 20px",
                display: "inline-block",
                border: "0px",
              },
            }}
          />
          <Routes>
            <Route path="*" element={<MainPage />} />
            <Route path="/" element={<MainPage />} />
            <Route path="/community/free" element={<BoardList />} />
            <Route path="/community/notice" element={<NoticeList />} />
            <Route path="/community/group" element={<GroupBoardList />} />
            {/* <Route path="/community/:type" element={<BoardList />} /> */}
            <Route path="/home" />
            <Route path="/board/free/:id" element={<BoardDetail />} />
            <Route path="/board/notice/:id" element={<BoardDetail />} />
            <Route path="/board/group/:id" element={<BoardDetail />} />
            <Route path="/board-form" element={<BoardForm />} />
            <Route path="/notice-form" element={<BoardForm />} />
            <Route path="/group-form" element={<GroupForm />} />
            <Route path="/board/:id" element={<BoardDetail />} />
            <Route path="/daily" element={<DailyPlanner />} />

            <Route path="/group/:id" element={<GroupPlanner />} />
            <Route path="/group/:id/glanner-board" element={<GlannerBoard />} />
            {/* 화상회의 페이지 */}
            <Route path="/conference/:id" element={<VideoConference />} />
            <Route path="/alarm" element={<AlarmPage />} />
          </Routes>
          <FloatingActionButtons />
        </Router>
      </Paper>
    </>
  );
}
