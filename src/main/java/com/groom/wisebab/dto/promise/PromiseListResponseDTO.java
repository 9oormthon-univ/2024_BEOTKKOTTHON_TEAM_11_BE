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
public class PromiseListResponseDTO {

    /**
     * 약속 리스트 조회 response DTO
     */

    private Long id;

    private String title;

    private State state;

    private String locName;

    private String ownerName;

    private LocalDate confirmedDate;

    private String confirmedTime;

    private List<PromiseMembersInnerResponseDTO> participants;
}
