package com.groom.wisebab.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    /**
     * 회원 가입의 request DTO
     */

    private String username;

    private String password;

    private String nickname;

}
