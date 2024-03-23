package com.groom.wisebab.dto.promise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PromisePaymentResponseDTO {

    /**
     * 송금 정보 조회
     */

    private String payMemo;

    private String kakaopayLink;

    private String bankAccount;
}
