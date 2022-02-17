package com.glanner.api.dto.request;

import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveUserReqDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @Builder
    public SaveUserReqDto(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(UserRoleStatus.ROLE_USER)
                .build();
    }
}
