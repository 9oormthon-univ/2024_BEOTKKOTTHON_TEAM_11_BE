package com.groom.wisebab.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferTimetableDTO {

    private String date;

    private List<Boolean> items;

}