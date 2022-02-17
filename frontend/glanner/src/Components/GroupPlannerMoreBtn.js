import React from "react";
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import EditIcon from '@mui/icons-material/Edit';
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

export const GroupPlannerMoreBtn = ({setHeadTitle}) => {
    const { pathname } = useLocation();
    const navigator = useNavigate();
    const deleteGlanner = () => {
        const id = pathname.slice(7)
        axios(`/api/glanner/${id}`, {method: 'DELETE'})
            .then(res => {
                alert(`${res.data.glannerName} 글래너가 삭제되었습니다.`)
                navigator(`/`)
            })
            .catch(err => console.log(err))
    }
    const editGlanner = () => {
        const newName = window.prompt("변경하려는 글래너 이름을 쓰세요.")
        const id = pathname.slice(7)
        axios(`/api/glanner/`, {method: 'PUT', data: {glannerId: id, glannerName: newName}})
            .then(res => setHeadTitle(newName))
            .catch(err => console.log(err))
    }
    return <span style={{marginLeft: '10px'}}>
        <DeleteForeverIcon onClick={deleteGlanner} />
        <EditIcon onClick={editGlanner} />
    </span>
}