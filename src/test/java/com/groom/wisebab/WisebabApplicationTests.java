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
		Long memberId = memberService.createMember("asdf", "dydrkfl11", "a49494273@gmail.com");
		Member member = memberService.findMemberById(memberId)
				.orElseThrow(
						NullPointerException::new
				);
		Long promiseId = promiseService.createPromise("밥약속", member, State.PENDING, "구름대학교", "구름광역시 구름대로99 23-5", 30000, "999999-99-9999");
		Promise promise = promiseService.findPromiseById(promiseId)
				.orElseThrow(
						NullPointerException::new
				);
		List<Integer> timeList = new ArrayList<>();
		timeList.add(1);
		timeList.add(2);
		timeList.add(3);
		preferTimetableService.createPreferTimetable(promise, member, timeList);

		List<Promise> allPromiseByState = promiseService.findAllPromiseByState(State.PENDING);
		System.out.println(allPromiseByState);
	}


}
