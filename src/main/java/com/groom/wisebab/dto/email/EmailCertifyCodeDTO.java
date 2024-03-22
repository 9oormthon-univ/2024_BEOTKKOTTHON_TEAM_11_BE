package com.groom.wisebab.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCertifyCodeDTO {

    /**
     * 이메일 코드가 일치하는지 검증하는 request DTO
     */

    private String email;

    private String univName;

    private int code;
}
