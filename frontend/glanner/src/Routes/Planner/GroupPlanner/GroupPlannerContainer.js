import React, { useEffect, useState } from "react";

import Helmet from "react-helmet";

import GroupPlannerPresenter from "./GroupPlannerPresenter";
import axios from "axios";
import Moment from "moment";

// import { useParams } from 'react-router-dom';
// import { useEffect } from "react";
import jwt_decode from "jwt-decode";

// export default function GroupPlannerContainer() {
//   const [eventList, setEventList] = React.useState(null);
//   const [usedId, setUserId] = React.useState("test2@naver.com");
//   const { id } = useParams();
//   const [glannerInfo, setGlannerInfo] = React.useState({});
//   const [latestBoard, setLatestBoard] = React.useState([]);
//   const [latestPlan, setlatestPlan] = React.useState([]);
//   const [authData, setAuthData] = React.useState('');

//   useEffect(() => {
//     axios(`/api/glanner/${id}`)
//       .then(res => {
//         setGlannerInfo(res.data)
//       })
//       .catch(err => console.log(err))
//     axios(`/api/glanner-board/${id}/0/3`)
//       .then(res => {
//         setLatestBoard(res.data)
//       })
//       .catch(err => console.log(err))
//     axios(`/api/glanner/${id}/${Moment().format('YYYY-MM-DD')}`)
//       .then(res => {
//         setlatestPlan(res.data)
//       })
//       .catch(err => console.log(err))

//     const token = localStorage.getItem('token');
//     const decoded = jwt_decode(token);
//     setAuthData(decoded)

//   }, [id])
//   // console.log(eventList);

//   // 멤버 삭제
//   const deleteMember = (user, userId) => {
//     console.log(user, userId)
//     const ok = window.confirm(`${user}님을 글래너에서 삭제하겠습니까?`)
//     if (ok) {
//       axios(`/api/glanner/user/${id}/${userId}`, {method: 'DELETE'})
//       .then(res => {
//         alert(`삭제되었습니다.`)
//         axios(`/api/glanner/${id}`)
//           .then(res => {
//             setGlannerInfo(res.data)
//           })
//           .catch(err => console.log(err))
//       })
//       .catch(err => {
//         alert('삭제할 수 없습니다.')
//         console.log(err)
//       })
//     }
//   }

import { useParams } from "react-router-dom";

export default function GroupPlannerContainer() {
  const [eventList, setEventList] = useState([
    {
      workId: 1,
      content: "알고리즘 스터디",
      end: "2022-02-14T17:00:00",
      start: "2022-02-14T15:00:00",
      title: "알고리즘 스터디",
    },
    {
      workId: 2,
      content: "과학 스터디",
      start: "2022-02-14T12:30",
      end: "2022-02-14T15:35",
      title: "과학 스터디",
    },
  ]);
  const groupPlannerId = useParams();
  const { id } = useParams();
  const [glannerInfo, setGlannerInfo] = useState({});
  const [latestBoard, setLatestBoard] = useState([]);
  const [latestPlan, setlatestPlan] = useState([]);
  const [authData, setAuthData] = React.useState('');

  const [date, setDate] = useState(
    new Date(new Date().getTime()).toISOString().substring(0, 10)
  );

  const [pickerOpen, setPickerOpen] = useState(false);
  const handlePickerOpen = () => setPickerOpen(true);
  const handlePickerClose = () => setPickerOpen(false);
  const [modalOpen, setModalOpen] = useState(false);
  const handleModalOpen = () => setModalOpen(true);
  const handleModalClose = () => setModalOpen(false);

  const fetchEventList = (moveDay) => {
    if (moveDay !== undefined) {
      setDate(moveDay);
      console.log(date);
    } else {
      const nowDate =
        new Date(new Date().getTime()).toISOString().substring(0, 8) + "01";
      setDate(nowDate);
    }

    axios
      .get(`/api/glanner/${groupPlannerId.id}/${date}`)
      .then((res) => {
        console.log(res.data);
        setEventList(res.data);
      })
      .catch(err => console.log(err))
    axios(`/api/glanner-board/${id}/0/3`)
      .then(res => {
        setLatestBoard(res.data)
      })
      .catch(err => console.log(err))
    axios(`/api/glanner/${id}/${Moment().format('YYYY-MM-DD')}`)
      .then(res => {
        setlatestPlan(res.data)
      })
      .catch(err => console.log(err))
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    const decoded = jwt_decode(token);
    setAuthData(decoded)

    axios
      .get(`/api/glanner/${groupPlannerId.id}/${date}`)
      .then((res) => {
        console.log(res.data);
        setEventList(res.data);
      })
      .catch(err => console.log(err))
    axios(`/api/glanner-board/${id}/0/3`)
      .then(res => {
        setLatestBoard(res.data)
      })
      .catch(err => console.log(err))
    axios(`/api/glanner/${id}/${Moment().format('YYYY-MM-DD')}`)
      .then(res => {
        setlatestPlan(res.data)
      })
      .catch(err => console.log(err))
    axios(`/api/glanner/${id}`)
      .then(res => {
        setGlannerInfo(res.data)
      })
      .catch(err => console.log(err))
  }, [id, date])

    // 멤버 삭제
  const deleteMember = (user, userId) => {
    // console.log(user, userId)
    const ok = window.confirm(`${user}님을 글래너에서 삭제하겠습니까?`)
    if (ok) {
      axios(`/api/glanner/user/${id}/${userId}`, {method: 'DELETE'})
      .then(res => {
        alert(`삭제되었습니다.`)
        axios(`/api/glanner/${id}`)
          .then(res => {
            setGlannerInfo(res.data)
          })
          .catch(err => console.log(err))
      })
      .catch(err => {
        alert('삭제할 수 없습니다.')
        console.log(err)
      })
    }
  }
  return (
    <>
      <Helmet>
        <title>Glanner | 그룹 플래너</title>
      </Helmet>
      <GroupPlannerPresenter
        numOfMember={glannerInfo.numOfMember}
        hostEmail={glannerInfo.hostEmail}
        membersInfos={glannerInfo.membersInfos}
        latestBoard={latestBoard}
        latestPlan={latestPlan}
        groupBoardId={glannerInfo.groupBoardId}
        deleteMember={deleteMember}
        authData={authData}
        groupPlannerId={groupPlannerId.id}
        eventList={eventList}
        handleModalOpen={handleModalOpen}
        handleModalClose={handleModalClose}
        modalOpen={modalOpen}
        handleEvent={fetchEventList}
      />
    </>
  );
}
