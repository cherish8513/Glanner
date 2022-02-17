import React, { useEffect, useState } from "react";
import { Link, useLocation, useParams } from "react-router-dom";

import styled from "styled-components";

import Grid from "@mui/material/Grid";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-solid-svg-icons";
import { ReactComponent as CalendarIcon } from "../assets/calendar-check-solid.svg";
import { ReactComponent as CircleUser } from "../assets/circle-user-solid.svg";
import axios from "axios";
import { GroupPlannerMoreBtn } from "./GroupPlannerMoreBtn";

const HeaderContainer = styled.div`
  word-break: break-all;
  height: 100px;
  min-width: 100%;
  overflow: hidden;
  font-size: 16px;
  font-family: 'Noto Sans KR' !important;
  color: 5f5f5f;
  width: 100%;
`;

const headerStyle = {
  display: "flex",
  alignItems: "center",
};

export default function Header({ title, host }) {
  const { pathname } = useLocation();
  const [headTitle, setHeadTitle] = useState('');
  const [hostEmail, setHostEMail] = useState('');
  const [glannerPage, setGlannerPage] = useState(false);
  useEffect(() => {
    if (pathname.includes('/community/group') || pathname.includes('/board/group/')) {
      setGlannerPage(false)
      setHeadTitle('그룹 찾기')
    } else if (pathname.includes('/community/free') || pathname.includes('/board/free/')) {
      setGlannerPage(false)
      setHeadTitle('자유 게시판')
    } else if (pathname.includes('/community/notice') || pathname.includes('/board/notice/')) {
      setGlannerPage(false)
      setHeadTitle('공지 게시판')
    } else if (pathname.includes('/group/')) {
      const id = pathname.slice(7)
      axios(`/api/glanner/${id}`)
        .then(res => {
          // console.log(res.data)
          setHeadTitle(res.data.glannerName)
          setHostEMail(res.data.hostEmail)
        })
        .catch(err => console.log(err))
        setGlannerPage(true)
    } else {
      setGlannerPage(false)
      setHeadTitle(title)
    }
  }, [pathname])

  // 글래너 이름 변경

  // 글래너 삭제
  return (
    <HeaderContainer>
      <div
        style={{
          marginLeft: "20px",
          lineHeight: 3.5,
          textAlign: "left",
          fontSize: "28px",
          width: "400px",
          float: "left",
          fontFamily: "Noto Sans KR",
        }}
      >
        {/* {title}  */}
        {headTitle}
        {glannerPage && hostEmail === host && <GroupPlannerMoreBtn setHeadTitle={setHeadTitle} />}
      </div>
      <div
        style={{
          height: "100%",
          textAlign: "right",
          minWidth: "300px",
          float: "right",
          display: "flex",
          alignItems: "center",
        }}
      >
        <Grid container direction="row">
          <Grid item xs={5} sx={headerStyle}>
            <CalendarIcon
              style={{
                fontSize: 25 + "px",
                color: "#5F5F5F",
                marginRight: "3px",
              }}
            />
            <Link to={`/daily`}>오늘의 일정</Link>
          </Grid>
          <Grid item xs={3} sx={headerStyle}>
            <FontAwesomeIcon
              icon={faBell}
              className="bell"
              style={{
                fontSize: 25 + "px",
                color: "#5F5F5F",
                marginRight: "3px",
              }}
            />
            <Link to={`/alarm`}>알림함</Link>
          </Grid>
          <Grid
            item
            xs={2}
            sx={{
              ml: "10px",
              pl: "20px",
              borderLeft: "1px solid #b5b5b5",
            }}
          >
            <CircleUser
              style={{
                fontSize: 40 + "px",
                color: "#5F5F5F",
                backgroundColor: "#F2D0D9",
                borderRadius: "50%",
              }}
            />
          </Grid>
        </Grid>
      </div>
    </HeaderContainer>
  );
}
