package com.groom.wisebab.dto.promise;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateToConfirmedDTO {

    /**
     * 상태를 확정으로 바꾸는 requestDTO
     */

    private LocalDate confirmedDate;

    private String confirmedTime;
}
