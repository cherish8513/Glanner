import React, { useEffect, useState } from "react";

import Helmet from "react-helmet";
import axios from "axios";
import jwt_decode from "jwt-decode";
import styled from "styled-components";

import AlarmPagePresenter from "./AlarmPagePresenter";

const AlarmDiv = styled.div``;

export default function AlarmPageContainer() {
  const [alarmList, setAlarmList] = useState();
  const [loading, setLoading] = useState(true);
  const bodyParams = {
    id: jwt_decode(localStorage.getItem("token")),
  };

  const getAlarmList = () => {
    axios
      .get("/api/notification", bodyParams)
      .then((res) => {
        setAlarmList(res.data);
        // console.log(res.data);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  };

  const readAlarmHandler = () => {
    axios.post("api/notification/status")
      .then((res) => {
        getAlarmList();
      })
      .catch((err) => console.log(err));
  }
  
  // * 오직 한 번만 component 마운트 때 로딩하기 위해서는 빈 배열을 뒤에 꼭 넣어줘야 한다.
  useEffect(() => {
    getAlarmList();
    readAlarmHandler();
  }, [])

  return (
    <>
      <Helmet>
        <title>Glanner | 알림</title>
      </Helmet>
      <AlarmPagePresenter alarmList={alarmList} readAlarmHandler={readAlarmHandler} loading={loading} />
    </>
  );
}
