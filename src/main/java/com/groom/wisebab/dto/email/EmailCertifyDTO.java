package com.groom.wisebab.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCertifyDTO {

    /**
     * 이메일 인증을 요청하는 request DTO
     */

    private String email;

    private String univName;
}
