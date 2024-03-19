package com.groom.wisebab;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.service.MemberService;
import com.groom.wisebab.service.PreferTimetableService;
import com.groom.wisebab.service.PromiseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class WisebabApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MemberService memberService;
	@Autowired
	private PromiseService promiseService;
	@Autowired
	private PreferTimetableService preferTimetableService;

	@Test
	void createTest() {

	}


}
