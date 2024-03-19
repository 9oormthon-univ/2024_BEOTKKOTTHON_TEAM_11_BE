package com.groom.wisebab.controller;

import com.groom.wisebab.dto.PreferTimetableDTO;
import com.groom.wisebab.service.PreferTimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PreferTimetableController {

    private final PreferTimetableService preferTimetableService;

    @PostMapping("/preferTimetable")
    public String submitPreferTimetable(@RequestBody List<PreferTimetableDTO> timetable) {

        System.out.println(preferTimetableService.flatPreferTimetable(timetable));
        return "success";
    }
}
