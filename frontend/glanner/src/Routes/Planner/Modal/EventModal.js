import { useState, useEffect } from "react";

import axios from "axios";
import { useNavigate } from "react-router-dom";

import styled from "styled-components";
import "react-datepicker/dist/react-datepicker.css";
import { Box, Typography, Modal, Grid } from "@mui/material";
import { InputUnstyled, TextareaAutosize } from "@mui/base";
import { styled as styledClass } from "@mui/system";
import { faCalendarPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { ReactComponent as XMark } from "../../../assets/xmark-solid.svg";
import DatePicker from "react-datepicker";

import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";

const modalStyle = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  maxWidth: 1300,
  minWidth: 800,
  bgcolor: "background.paper",
  px: 1.5,
  py: 2,
  borderRadius: 2,
  border: "0px solid #FFFFFF",
  fontFamily: "Noto Sans KR, sans-serif",
};
const SDatePicker = styled(DatePicker)`
  margin-top: 10px;
  font-size: 0.875rem;
  font-family: "Noto Sans KR", sans-serif;
  font-weight: 400;
  line-height: 1.5;
  border-radius: 5px;
  height: 50px;
  width: 100%;
  padding: 4px 12px;
`;

const ModalHeaderDiv = styled.div`
  width: 100%;
  border-bottom: 2px solid #e5e5e5;
  display: flex;
  padding-bottom: 5px;
  font-family: "Noto Sans KR, sans-serif";
`;

const ModalFooterDiv = styled.div`
  width: 100%;
  text-align: right;
`;

const SubmitButton = styled.button`
  background-color: #8c7b80;
  color: #ffffff;
  font-size: 18px;
  line-height: 14px;
  border: 0px solid #8c7b80;
  border-radius: 6px;
  height: 35px;
  width: 55px;
  font-family: "Noto Sans KR, sans-serif";
  &:hover {
    color: #ffffff;
    background-color: #8c7b8066;
    border: 0px solid #8c7b8066;
  }
`;

const DeleteButton = styled.button`
  background-color: #ac7b8022;
  color: #ac7b80;
  font-size: 18px;
  font-family: "Noto Sans KR, sans-serif";
  line-height: 14px;
  border: 0px solid #8c7b80;
  border-radius: 6px;
  margin-left: 4px;
  height: 35px;
  width: 55px;
  &:hover {
    color: #ffffff;
    background-color: #8c7b8066;
    border: 0px solid #8c7b8066;
  }
`;

const blue = {
  200: "#80BFFF",
  400: "#3399FF",
};

const grey = {
  50: "#F3F6F9",
  100: "#E7EBF0",
  200: "#E0E3E7",
  300: "#CDD2D7",
  400: "#B2BAC2",
  500: "#A0AAB4",
  600: "#6F7E8C",
  700: "#3E5060",
  800: "#2D3843",
  900: "#1A2027",
};

const StyledInputElement = styledClass("input")(
  ({ theme }) => `
  width: 100%;
  font-size: 0.875rem;
  font-family: "Noto Sans KR", sans-serif;
  font-weight: 400;
  line-height: 1.5;
  color: ${theme.palette.mode === "dark" ? grey[300] : grey[900]};
  background: ${theme.palette.mode === "dark" ? grey[900] : "#FFFFFF"};
  border: 1px solid ${theme.palette.mode === "dark" ? grey[800] : "#E5E5E5"};
  border-radius: 8px;
  padding: 4px 12px;
  transition: all 150ms ease;
  box-shadow: 0.5px 1px 2px 0.5px rgba(130, 130, 130, 0.3);

  &:hover {
    background: ${theme.palette.mode === "dark" ? "" : grey[100]};
    border-color: ${theme.palette.mode === "dark" ? grey[700] : grey[400]};
  }

  &:focus {
    outline: 2px solid ${theme.palette.mode === "dark" ? blue[400] : blue[200]};
    outline-offset: 2px;
  }
`
);

const formTextStyle = {
  fontSize: "16px",
  color: "#5F5F5F",
};

const reserveRadioStyle = {
  fontSize: "16px",
  color: "#262626",
  lineHeight: 2,
  ml: 1,
};

export default function EventModal({
  handleClose,
  open,
  specificEvent,
  eventId,
  type,
  groupPlannerId,
  handleEvent,
}) {
  const navigate = useNavigate();
  const [data, setData] = useState({
    title: "",
    content: "",
    startDate: "",
    endDate: "",
    alarmDate: "",
  });

  useEffect(() => {
    if (specificEvent !== null) {
      setData(specificEvent);
    }
  }, [data, specificEvent]);

  const handleSubmit = (e) => {
    e.preventDefault();
    data.startDate =
      dateToString(StartDate) + " " + TimeToString(StartTime, StartMinute);
    data.endDate =
      dateToString(EndDate) + " " + TimeToString(EndTime, EndMinute);
    if (data.title === "") {
      alert("제목을 입력해주세요.");
      return;
    }
    if (data.content === "") {
      alert("내용을 입력해주세요.");
      return;
    }
    if (data.startDate === "") {
      alert("시작일을 입력해주세요.");
      return;
    }
    if (data.endDate === "") {
      alert("마감일을 입력해주세요.");
      return;
    }

    // 기존 이벤트 수정
    if (specificEvent != null) {
      if (type === "groupPlanner") {
        const newData = { ...data, workId: eventId };
        console.log(newData);
        axios
          .put("/api/glanner/work", newData)
          .then((res) => {
            alert("수정 성공!");
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            console.log(res.data);
            setData({});
            handleClose();
            navigate(`/group/${groupPlannerId}`);
          })
          .catch((err) => {
            console.log(err);
            alert("수정 실패!");
          });
      } else if (type === "myPlanner") {
        axios
          .put(`/api/user/planner/work/${eventId}`, data)
          .then((res) => {
            alert("수정 성공!");
            console.log(res.data);
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            setData({});
            handleClose();
            navigate("/");
          })
          .catch((err) => {
            console.log(err);
            alert("수정 실패!");
          });
      }
    } else if (specificEvent == null) {
      // 새로운 일정 추가
      if (type === "groupPlanner") {
        const newData = {...data, glannerId: groupPlannerId}
        axios
          .post("/api/glanner/work", newData)
          .then((res) => {
            alert("작성 성공!");
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            console.log(res.data);
            setData({});
            handleClose();
            navigate(`/group/${groupPlannerId}`);
          })
          .catch((err) => {
            console.log(err);
            alert("작성 실패!");
          });
      } else if (type === "myPlanner") {
        console.log(data);
        axios
          .post("/api/user/planner/work", data)
          .then((res) => {
            alert("작성 성공!");
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            console.log(res.data);
            setData({});
            handleClose();
            navigate("/");
          })
          .catch((err) => {
            console.log(err.toJSON());
            alert("작성 실패!");
          });
      }
    }
  };

  const handleDelete = (e) => {
    if (specificEvent && eventId) {
      if (type === "myPlanner") {
        axios
          .delete(`/api/user/planner/work/${eventId}`)
          .then((res) => {
            alert("삭제 성공!");
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            console.log(res.data);
            setData({});
            handleClose();
            navigate("/");
            e.preventDefault();
          })
          .catch((err) => {
            console.log(err);
          });
      } else if (type === "groupPlanner" && groupPlannerId) {
        axios
          .delete(`/api/user/planner/work/${groupPlannerId}/${eventId}`)
          .then((res) => {
            alert("삭제 성공!");
            if (handleEvent) {
              handleEvent(String(data.startDate).substring(0, 8) + "01");
            }
            console.log(res.data);
            setData({});
            handleClose();
            navigate(`/group/${groupPlannerId}`);
            e.preventDefault();
          })
          .catch((err) => {
            console.log(err);
          });
      }
    }
  };

  const handle = (e) => {
    const newData = { ...data };
    newData[e.target.id] = e.target.value;
    setData(newData);
  };
  const [StartDate, setStartDate] = useState(new Date());
  const [EndDate, setEndDate] = useState(new Date());
  const [StartTime, setStartTime] = useState("");
  const [StartMinute, setStartMinute] = useState("");
  const [EndTime, setEndTime] = useState("");
  const [EndMinute, setEndMinute] = useState("");

  const handleStartTimeChange = (event) => {
    setStartTime(event.target.value);
  };
  const handleStartMinuteChange = (event) => {
    setStartMinute(event.target.value);
  };
  const handleEndTimeChange = (event) => {
    setEndTime(event.target.value);
  };
  const handleEndMinuteChange = (event) => {
    setEndMinute(event.target.value);
  };
  const dateToString = (date) => {
    return (
      date.getFullYear() +
      "-" +
      (date.getMonth() + 1).toString().padStart(2, "0") +
      "-" +
      date.getDate().toString().padStart(2, "0")
    );
  };
  const TimeToString = (Time, Minute) => {
    var t, m;
    if (Time < 10) {
      t = '0' + Time;
    }
    else {
      t = Time;
    }
    if (Minute < 10) {
      m = '0' + Minute;
    }
    else {
      m = Minute;
    }
    return t + ':' + m;
  }

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <ModalHeaderDiv>
          <div
            style={{
              width: "50%",
              textAlign: "left",
              color: "#262626",
              display: "flex",
              fontSize: "22px",
            }}
          >
            <FontAwesomeIcon
              icon={faCalendarPlus}
              className="calendarPlus"
              style={{
                color: "#8C7B80",
                fontSize: "25px",
                padding: "2px",
                marginRight: "3",
                marginLeft: "5",
                textAlign: "center",
                borderRadius: "2px",
              }}
            />
            <h3 style={{ lineHeight: "27px" }}>
              {type === "myPlanner" ? "개인" : "그룹"}{" "}
              {specificEvent ? "일정 수정하기" : "일정 추가하기"}
            </h3>
          </div>
          <div style={{ width: "50%", textAlign: "right" }}>
            <button
              style={{
                border: "0",
                outline: "0",
                backgroundColor: "rgba(0,0,0,0)",
              }}
              onClick={handleClose}
            >
              <XMark
                style={{
                  color: "#959595",
                  fontSize: "25px",
                  padding: "2px",
                  textAlign: "center",
                }}
              />
            </button>
          </div>
        </ModalHeaderDiv>

        {/* 폼 작성 부분 */}
        <Box
          sx={{
            color: "#5F5F5F",
            pt: 3,
            px: 4,
            fontFamily: "Noto Sans KR, sans-serif",
          }}
        >
          <form noValidate onSubmit={handleSubmit} method="POST">
            <input
              type="hidden"
              value={data.workId ? data.workId : data.glannerWorkId}
              id={data.workId ? "workId" : "glannerWorkId"}
            />
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="title" sx={formTextStyle}>
                  제목
                </Typography>
              </Grid>
              <Grid item xs={9}>
                <InputUnstyled
                  components={{ Input: StyledInputElement }}
                  id="title"
                  onChange={(e) => handle(e)}
                  value={data.title}
                />
              </Grid>
            </Grid>
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="start" sx={formTextStyle}>
                  시작일
                </Typography>
              </Grid>
              <Grid item xs={5}>
                <SDatePicker
                  selected={StartDate}
                  onChange={(date) => setStartDate(date)}
                  dateFormat="yyyy-MM-dd(eee)"
                  minDate={new Date()}
                />
              </Grid>
              <Grid item xs={2}>
                <FormControl sx={{ m: 1, minWidth: 110 }}>
                  <InputLabel id="demo-simple-select-helper-label">
                    Time
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-helper-label"
                    id="demo-simple-select-helper"
                    value={StartTime}
                    label="StartTime"
                    onChange={handleStartTimeChange}
                  >
                    <MenuItem value=""></MenuItem>
                    <MenuItem value={1}>01</MenuItem>
                    <MenuItem value={2}>02</MenuItem>
                    <MenuItem value={3}>03</MenuItem>
                    <MenuItem value={4}>04</MenuItem>
                    <MenuItem value={5}>05</MenuItem>
                    <MenuItem value={6}>06</MenuItem>
                    <MenuItem value={7}>07</MenuItem>
                    <MenuItem value={8}>08</MenuItem>
                    <MenuItem value={9}>09</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                    <MenuItem value={11}>11</MenuItem>
                    <MenuItem value={12}>12</MenuItem>
                    <MenuItem value={13}>13</MenuItem>
                    <MenuItem value={14}>14</MenuItem>
                    <MenuItem value={15}>15</MenuItem>
                    <MenuItem value={16}>16</MenuItem>
                    <MenuItem value={17}>17</MenuItem>
                    <MenuItem value={18}>18</MenuItem>
                    <MenuItem value={19}>19</MenuItem>
                    <MenuItem value={20}>20</MenuItem>
                    <MenuItem value={21}>21</MenuItem>
                    <MenuItem value={22}>22</MenuItem>
                    <MenuItem value={23}>23</MenuItem>
                    <MenuItem value={24}>24</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={2}>
                <FormControl sx={{ m: 1, minWidth: 110 }}>
                  <InputLabel id="demo-simple-select-helper-label">
                    Minute
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-helper-label"
                    id="demo-simple-select-helper"
                    value={StartMinute}
                    label="StartMinute"
                    onChange={handleStartMinuteChange}
                  >
                    <MenuItem value=""></MenuItem>
                    <MenuItem value={0}>00</MenuItem>
                    <MenuItem value={1}>01</MenuItem>
                    <MenuItem value={2}>02</MenuItem>
                    <MenuItem value={3}>03</MenuItem>
                    <MenuItem value={4}>04</MenuItem>
                    <MenuItem value={5}>05</MenuItem>
                    <MenuItem value={6}>06</MenuItem>
                    <MenuItem value={7}>07</MenuItem>
                    <MenuItem value={8}>08</MenuItem>
                    <MenuItem value={9}>09</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                    <MenuItem value={11}>11</MenuItem>
                    <MenuItem value={12}>12</MenuItem>
                    <MenuItem value={13}>13</MenuItem>
                    <MenuItem value={14}>14</MenuItem>
                    <MenuItem value={15}>15</MenuItem>
                    <MenuItem value={16}>16</MenuItem>
                    <MenuItem value={17}>17</MenuItem>
                    <MenuItem value={18}>18</MenuItem>
                    <MenuItem value={19}>19</MenuItem>
                    <MenuItem value={20}>20</MenuItem>
                    <MenuItem value={21}>21</MenuItem>
                    <MenuItem value={22}>22</MenuItem>
                    <MenuItem value={23}>23</MenuItem>
                    <MenuItem value={24}>24</MenuItem>
                    <MenuItem value={25}>25</MenuItem>
                    <MenuItem value={26}>26</MenuItem>
                    <MenuItem value={27}>27</MenuItem>
                    <MenuItem value={28}>28</MenuItem>
                    <MenuItem value={29}>29</MenuItem>
                    <MenuItem value={30}>30</MenuItem>
                    <MenuItem value={31}>31</MenuItem>
                    <MenuItem value={32}>32</MenuItem>
                    <MenuItem value={33}>33</MenuItem>
                    <MenuItem value={34}>34</MenuItem>
                    <MenuItem value={35}>35</MenuItem>
                    <MenuItem value={36}>36</MenuItem>
                    <MenuItem value={37}>37</MenuItem>
                    <MenuItem value={38}>38</MenuItem>
                    <MenuItem value={39}>39</MenuItem>
                    <MenuItem value={40}>40</MenuItem>
                    <MenuItem value={41}>41</MenuItem>
                    <MenuItem value={42}>42</MenuItem>
                    <MenuItem value={43}>43</MenuItem>
                    <MenuItem value={44}>44</MenuItem>
                    <MenuItem value={45}>45</MenuItem>
                    <MenuItem value={46}>46</MenuItem>
                    <MenuItem value={47}>47</MenuItem>
                    <MenuItem value={48}>48</MenuItem>
                    <MenuItem value={49}>49</MenuItem>
                    <MenuItem value={50}>50</MenuItem>
                    <MenuItem value={51}>51</MenuItem>
                    <MenuItem value={52}>52</MenuItem>
                    <MenuItem value={53}>53</MenuItem>
                    <MenuItem value={54}>54</MenuItem>
                    <MenuItem value={55}>55</MenuItem>
                    <MenuItem value={56}>56</MenuItem>
                    <MenuItem value={57}>57</MenuItem>
                    <MenuItem value={58}>58</MenuItem>
                    <MenuItem value={59}>59</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="end" sx={formTextStyle}>
                  마감일
                </Typography>
              </Grid>
              <Grid item xs={5}>
                <SDatePicker
                  selected={EndDate}
                  onChange={(date) => setEndDate(date)}
                  dateFormat="yyyy-MM-dd(eee)"
                  minDate={StartDate}
                />
              </Grid>
              <Grid item xs={2}>
                <FormControl sx={{ m: 1, minWidth: 110 }}>
                  <InputLabel id="demo-simple-select-helper-label">
                    Time
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-helper-label"
                    id="demo-simple-select-helper"
                    value={EndTime}
                    label="EndTime"
                    onChange={handleEndTimeChange}
                  >
                    <MenuItem value=""></MenuItem>
                    <MenuItem value={1}>01</MenuItem>
                    <MenuItem value={2}>02</MenuItem>
                    <MenuItem value={3}>03</MenuItem>
                    <MenuItem value={4}>04</MenuItem>
                    <MenuItem value={5}>05</MenuItem>
                    <MenuItem value={6}>06</MenuItem>
                    <MenuItem value={7}>07</MenuItem>
                    <MenuItem value={8}>08</MenuItem>
                    <MenuItem value={9}>09</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                    <MenuItem value={11}>11</MenuItem>
                    <MenuItem value={12}>12</MenuItem>
                    <MenuItem value={13}>13</MenuItem>
                    <MenuItem value={14}>14</MenuItem>
                    <MenuItem value={15}>15</MenuItem>
                    <MenuItem value={16}>16</MenuItem>
                    <MenuItem value={17}>17</MenuItem>
                    <MenuItem value={18}>18</MenuItem>
                    <MenuItem value={19}>19</MenuItem>
                    <MenuItem value={20}>20</MenuItem>
                    <MenuItem value={21}>21</MenuItem>
                    <MenuItem value={22}>22</MenuItem>
                    <MenuItem value={23}>23</MenuItem>
                    <MenuItem value={24}>24</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={2}>
                <FormControl sx={{ m: 1, minWidth: 110 }}>
                  <InputLabel id="demo-simple-select-helper-label">
                    Minute
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-helper-label"
                    id="demo-simple-select-helper"
                    value={EndMinute}
                    label="EndMinute"
                    onChange={handleEndMinuteChange}
                  >
                    <MenuItem value=""></MenuItem>
                    <MenuItem value={0}>00</MenuItem>
                    <MenuItem value={1}>01</MenuItem>
                    <MenuItem value={2}>02</MenuItem>
                    <MenuItem value={3}>03</MenuItem>
                    <MenuItem value={4}>04</MenuItem>
                    <MenuItem value={5}>05</MenuItem>
                    <MenuItem value={6}>06</MenuItem>
                    <MenuItem value={7}>07</MenuItem>
                    <MenuItem value={8}>08</MenuItem>
                    <MenuItem value={9}>09</MenuItem>
                    <MenuItem value={10}>10</MenuItem>
                    <MenuItem value={11}>11</MenuItem>
                    <MenuItem value={12}>12</MenuItem>
                    <MenuItem value={13}>13</MenuItem>
                    <MenuItem value={14}>14</MenuItem>
                    <MenuItem value={15}>15</MenuItem>
                    <MenuItem value={16}>16</MenuItem>
                    <MenuItem value={17}>17</MenuItem>
                    <MenuItem value={18}>18</MenuItem>
                    <MenuItem value={19}>19</MenuItem>
                    <MenuItem value={20}>20</MenuItem>
                    <MenuItem value={21}>21</MenuItem>
                    <MenuItem value={22}>22</MenuItem>
                    <MenuItem value={23}>23</MenuItem>
                    <MenuItem value={24}>24</MenuItem>
                    <MenuItem value={25}>25</MenuItem>
                    <MenuItem value={26}>26</MenuItem>
                    <MenuItem value={27}>27</MenuItem>
                    <MenuItem value={28}>28</MenuItem>
                    <MenuItem value={29}>29</MenuItem>
                    <MenuItem value={30}>30</MenuItem>
                    <MenuItem value={31}>31</MenuItem>
                    <MenuItem value={32}>32</MenuItem>
                    <MenuItem value={33}>33</MenuItem>
                    <MenuItem value={34}>34</MenuItem>
                    <MenuItem value={35}>35</MenuItem>
                    <MenuItem value={36}>36</MenuItem>
                    <MenuItem value={37}>37</MenuItem>
                    <MenuItem value={38}>38</MenuItem>
                    <MenuItem value={39}>39</MenuItem>
                    <MenuItem value={40}>40</MenuItem>
                    <MenuItem value={41}>41</MenuItem>
                    <MenuItem value={42}>42</MenuItem>
                    <MenuItem value={43}>43</MenuItem>
                    <MenuItem value={44}>44</MenuItem>
                    <MenuItem value={45}>45</MenuItem>
                    <MenuItem value={46}>46</MenuItem>
                    <MenuItem value={47}>47</MenuItem>
                    <MenuItem value={48}>48</MenuItem>
                    <MenuItem value={49}>49</MenuItem>
                    <MenuItem value={50}>50</MenuItem>
                    <MenuItem value={51}>51</MenuItem>
                    <MenuItem value={52}>52</MenuItem>
                    <MenuItem value={53}>53</MenuItem>
                    <MenuItem value={54}>54</MenuItem>
                    <MenuItem value={55}>55</MenuItem>
                    <MenuItem value={56}>56</MenuItem>
                    <MenuItem value={57}>57</MenuItem>
                    <MenuItem value={58}>58</MenuItem>
                    <MenuItem value={59}>59</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="content" sx={formTextStyle}>
                  설명
                </Typography>
              </Grid>
              <Grid item xs={9}>
                <TextareaAutosize
                  maxRows={3}
                  minRows={3}
                  id="content"
                  style={{
                    width: "100%",
                    fontSize: "16px",
                    color: "#959595",
                    border: "1px solid #E5E5E5",
                    borderRadius: "8px",
                    boxShadow: "0.5px 1px 2px 0.5px rgba(130, 130, 130, 0.3)",
                  }}
                  onChange={(e) => {
                    handle(e);
                  }}
                  value={data.content}
                />
              </Grid>
            </Grid>
            {/* <Grid container sx={{ mb: 2 }}>
                <Grid item xs={3} sx={{ textAlign: "left" }}>
                  <Typography className="category" sx={formTextStyle}>
                    카테고리
                  </Typography>
                </Grid>
                <Grid item xs={9}>
                  <InputUnstyled
                    type="select"
                    components={{ Input: StyledInputElement }}
                  />
                </Grid>
              </Grid> */}
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="alarm" sx={formTextStyle}>
                  알림
                </Typography>
              </Grid>
              <Grid item xs={9}>
                <InputUnstyled
                  id="alarmDate"
                  components={{ Input: StyledInputElement }}
                  onChange={(e) => handle(e)}
                  value={data.alarmDate}
                />
              </Grid>
            </Grid>
            <Grid container sx={{ mb: 2 }}>
              <Grid item xs={3} sx={{ textAlign: "left" }}>
                <Typography className="alarm" sx={formTextStyle}>
                  화상회의 예약
                </Typography>
              </Grid>
              <Grid item xs={9} sx={{ display: "flex" }}>
                <Grid item xs={3} sx={{ display: "flex" }}>
                  <InputUnstyled
                    type="radio"
                    id="yes"
                    name="conferenceReserve"
                  />
                  <Typography sx={reserveRadioStyle}>YES</Typography>
                </Grid>
                <Grid item xs={3} sx={{ display: "flex" }}>
                  <InputUnstyled
                    type="radio"
                    id="no"
                    name="conferenceReserve"
                  />
                  <Typography sx={reserveRadioStyle}>NO</Typography>
                </Grid>
              </Grid>
            </Grid>
            <ModalFooterDiv>
              <SubmitButton type="submit">OK</SubmitButton>
              {specificEvent ? (
                <DeleteButton
                  type="button"
                  onClick={(e) => {
                    handleDelete(e);
                  }}
                >
                  삭제
                </DeleteButton>
              ) : (
                ""
              )}
            </ModalFooterDiv>
          </form>
        </Box>
      </Box>
    </Modal>
  );
}
