package com.groom.wisebab.dto.promise;

import com.groom.wisebab.domain.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class PromiseDetailResponseDTO {

    /**
     * 약속의 상세정보 response DTO
     */

    private Long id;

    private State state;

    private String title;

    private String ownerName;

    private Boolean isLeader;

    private LocalDate confirmedDate;

    private String confirmedTime;

    private String locName;

    private String locAddress;

    private LocalDate startDate;

    private LocalDate endDate;

    private String memo;

    private List<PromiseMembersInnerResponseDTO> participants;

    private int confirmedPeopleCount;

    private Boolean allResponded;


}
