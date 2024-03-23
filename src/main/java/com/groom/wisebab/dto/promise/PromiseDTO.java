package com.groom.wisebab.dto.promise;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class PromiseDTO {

    /**
     * 약속 생성하는 request DTO
     */

    private String title;

    private Long ownerId;

    private String locName;

    private String locAddress;

    private LocalDate startDate;

    private LocalDate endDate;

    private String memo;
}
