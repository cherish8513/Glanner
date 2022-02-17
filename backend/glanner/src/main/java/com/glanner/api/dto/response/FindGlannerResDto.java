package com.glanner.api.dto.response;

import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.glanner.UserGlanner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FindGlannerResDto {
    Long glannerId;
    String hostEmail;
    String glannerName;
    Long groupBoardId;
    int numOfMember;
    List<MemberInfo> membersInfos = new ArrayList<>();

    public FindGlannerResDto(Glanner glanner, GroupBoard groupBoard, List<UserGlanner> userGlanners){
        glannerId = glanner.getId();
        hostEmail = glanner.getHost().getEmail();
        glannerName = glanner.getName();
        groupBoardId = groupBoard.getId();
        numOfMember = userGlanners.size();
        userGlanners.forEach(u -> {membersInfos.add(new MemberInfo(u.getUser().getId(), u.getUser().getEmail(), u.getUser().getName()));});
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfo{
        Long userId;
        String userEmail;
        String userName;
    }
}
