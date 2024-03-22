package com.groom.wisebab.dto.promise;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateToExpiredDTO {

    /**
     * 상태를 종료된 밥약으로 바꾸는 request DTO
     */

    private String bankAccount;

    private String kakaopayLink;

    private String payMemo;
}
