package com.groom.wisebab.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponseDTO {

    private Long id;

    private String username;

    private String nickname;
}
